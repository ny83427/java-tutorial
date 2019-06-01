import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Download jar and source file of a given library, plus its dependencies
 */
public class DependencyDownloader {

    private static final String DEFAULT_URL = "http://central.maven.org/maven2/org/seleniumhq/selenium/selenium-java/3.141.59/";

    private static final int MAX_ATTEMPTS = 3;

    private enum FileType {
        POM, JAR, SOURCE, JAVADOC
    }

    private static class Dependency {
        private static final String PREFIX = "http://central.maven.org/maven2/";

        private String postFix(FileType fileType) {
            String prefix = groupPath + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version;
            switch (fileType) {
                case POM:
                    return prefix + ".pom";
                case JAR:
                    return prefix + ".jar";
                case SOURCE:
                    return prefix + "-sources.jar";
                case JAVADOC:
                    return prefix + "-javadoc.jar";
                default:
                    throw new IllegalArgumentException("Unknown file type: " + fileType);
            }

        }

        private String url(FileType fileType) {
            return PREFIX + postFix(fileType);
        }

        private File filePath(FileType fileType) {
            return new File("tmp", postFix(fileType));
        }

        private final String groupId, artifactId, version;

        private final String groupPath;

        private Dependency(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            this.groupPath = this.groupId.replace('.', '/');
        }

        private Dependency(String url) {
            String rest = url.replace(PREFIX, "");
            if (rest.charAt(rest.length() - 1) == '/') rest = rest.substring(0, rest.length() - 1);
            int prev = rest.lastIndexOf('/');
            this.version = rest.substring(prev + 1);

            rest = rest.substring(0, prev);
            prev = rest.lastIndexOf('/');
            this.artifactId = rest.substring(prev + 1);
            this.groupPath = rest.substring(0, prev);
            this.groupId = this.groupPath.replace('/', '.');
        }

        @Override
        public String toString() {
            return groupId + ":" + artifactId + ":" + version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Dependency that = (Dependency) o;
            return Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId) &&
                Objects.equals(version, that.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(groupId, artifactId, version);
        }

        private void mkdir() {
            File dir = new File("tmp/" + groupPath);
            if (dir.exists() && dir.isDirectory()) return;
            dir.mkdirs();
        }

        private boolean checkMd5(File jar) throws IOException {
            if (!jar.exists() || !jar.isFile()) return false;
            File md5 = new File(jar.getParent(), jar.getName() + ".md5");
            if (!md5.exists() || !md5.isFile()) return false;

            byte[] bytes = Files.readAllBytes(jar.toPath());
            String curr = DigestUtils.md5Hex(bytes);
            String expected = Files.readString(md5.toPath());
            if (!curr.equals(expected)) {
                System.err.printf("%s MD5 doesn't match: %s VS %s%n", jar.getName(), curr, expected);
                return false;
            } else {
                return true;
            }
        }
    }

    public static void main(String[] args) {
        String url = args.length > 0 ? args[0] : DEFAULT_URL;
        final long now = System.currentTimeMillis();
        Dependency dependency = new Dependency(url);
        Set<Dependency> visited = new HashSet<>();
        download(dependency, visited);
        System.out.printf("%d library files downloaded in %.2fS%n",
            visited.size(), (System.currentTimeMillis() - now) / 1000.0);
        visited.forEach(System.out::println);
    }

    private static void download(Dependency dep, Set<Dependency> visited) {
        if (visited.contains(dep)) return;

        try {
            visited.add(dep);
            dep.mkdir();
            for (FileType ft : FileType.values()) {
                if (!dep.checkMd5(dep.filePath(ft))) {
                    String url = dep.url(ft);
                    File file = dep.filePath(ft);
                    download(url, file);
                    download(url + ".md5", new File(file.getAbsolutePath() + ".md5"));
                } else {
                    System.out.printf("%s -> %s processed already!%n", dep, ft);
                }
            }

            Document doc = Jsoup.parse(Files.readString(dep.filePath(FileType.POM).toPath()));
            Elements properties = doc.select("properties");
            Map<String, String> map = new HashMap<>();
            for (Element e : properties) {
                map.put(e.tagName(), e.val().trim());
            }

            Elements elements = doc.select("dependencies dependency");
            List<Dependency> list = new ArrayList<>(elements.size());
            for (Element e : elements) {
                String scope = e.select("scope").text();
                // eg: <systemPath>${java.home}/../src.zip</systemPath>
                if ("system".equals(scope))
                    continue;
                String version = e.select("version").text().trim();
                if (version.startsWith("${")) {
                    version = map.getOrDefault(version.substring(2, version.length() - 1), "");
                }

                Dependency d = new Dependency(e.select("groupId").text().trim(),
                    e.select("artifactId").text().trim(), version);
                if (d.groupId.isBlank() || d.artifactId.isBlank() || d.version.isBlank()) {
                    System.err.println(dep + "->" + e.html().replace("\n", "") + " is not supported currently.");
                    continue;
                }
                list.add(d);
            }

            list.forEach(e -> download(e, visited));
        } catch (IOException e) {
            System.err.println(dep + "->" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void download(String url, File file) throws IOException {
        file.getParentFile().mkdirs();

        IOException ex = null;
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                byte[] bytes = Jsoup.connect(url).timeout(6000).ignoreContentType(true).execute().bodyAsBytes();
                file.createNewFile();
                Files.write(file.toPath(), bytes);
                return;
            } catch (IOException e) {
                ex = e;
            }
        }
        throw ex;
    }

}
