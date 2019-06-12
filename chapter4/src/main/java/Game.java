import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game extends JFrame {
    private static final int DELAY_TIME = 240;

    private final GameBoard board;

    private final JButton startBtn, resetBtn, exitBtn;

    private final JLabel scoreLabel;

    private final Font font = new Font("Default", Font.BOLD, 18);

    private Game(GameBoard board) {
        this.board = board;
        this.startBtn = new JButton("Start Game", new ImageIcon(this.getClass().getResource("/start.png")));
        this.resetBtn = new JButton("Reset Game", new ImageIcon(this.getClass().getResource("/reset.png")));
        this.exitBtn = new JButton("Exit Game", new ImageIcon(this.getClass().getResource("/exit.png")));
        this.scoreLabel = new JLabel();
        this.scoreLabel.setFont(font);

        this.initComponents();

        int maxHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
        int locationY = (maxHeight - this.getHeight()) / 2;
        if (locationY < 0) {
            locationY = 0;
        }
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2, locationY);
    }

    private void setExtraAttributesForButtons(JButton... buttons) {
        for (JButton button : buttons) {
            button.setBorder(null);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(false);

            button.setFont(font);
        }
    }

    private void initComponents() {
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/icon.png"));
        this.setTitle("The Most Boring Soccer Game In History");
        this.setIconImage(icon.getImage());
        this.setResizable(false);
        this.setExtraAttributesForButtons(startBtn, resetBtn, exitBtn);

        startBtn.addActionListener(e -> startGame());
        resetBtn.addActionListener(e -> resetGame());
        exitBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "Are you sure to exit game?",
                "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GroupLayout mainPanelLayout = new GroupLayout(board);
        board.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addComponent(board, Constants.FIELD_WIDTH, Constants.FIELD_WIDTH, Constants.FIELD_WIDTH)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20).addComponent(scoreLabel, 300, 300, 300)
                    .addComponent(startBtn).addGap(20).addComponent(resetBtn).addGap(20).addComponent(exitBtn))
        );

        final int btnH = 40;
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(board, Constants.FIELD_HEIGHT + btnH, Constants.FIELD_HEIGHT + btnH, Constants.FIELD_HEIGHT + btnH)
                    .addGap(5)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(scoreLabel, btnH, btnH, btnH)
                        .addComponent(startBtn, btnH, btnH, btnH)
                        .addComponent(resetBtn, btnH, btnH, btnH)
                        .addComponent(exitBtn, btnH, btnH, btnH)))
        );
        pack();
    }

    private void resetGame() {
        board.reset();
    }

    private void startGame() {
        startBtn.setEnabled(false);
        board.start();
        if (BG_PLAYER != null) {
            BG_PLAYER.stop();
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                while (board.isRunning()) {
                    try {
                        Tools.sleepSilently(DELAY_TIME);
                        board.triggerEvent();
                        board.repaint();
                        scoreLabel.setText(board.getScore().abbrev());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                startBtn.setEnabled(true);
                return null;
            }
        }.execute();
    }

    public static void main(String[] args) {
        Platform.startup(() -> {});
        final GameBoard board = new GameBoard();
        // If splash has been set via VM options, skip text mode
        // java -splash:images/champions.jpg Game
        if (SplashScreen.getSplashScreen() != null ||
            args.length > 0 && args[0].equalsIgnoreCase("Start")) {
            board.applyDefaultSettings();
            initGame(board);
            return;
        }

        ColoredPrinter cp = new ColoredPrinter.Builder(1, false)
            .foreground(FColor.GREEN).background(BColor.BLACK).build();

        System.out.println("===============================================");
        System.out.println("=                                             =");
        System.out.println("=   WELCOME TO THE MOST BORING SOCCER GAME!   =");
        System.out.println("=   YOU WILL EXPERIENCE THE PUREST JOY HERE   =");
        System.out.println("=                                             =");
        System.out.println("=         Nathanael Yang ©2018 – 2019         =");
        System.out.println("=                                             =");
        System.out.println("===============================================");
        System.out.println("= -> Input 'Start' to begin a new game.       =");
        System.out.println("= -> Input 'Default' to begin pre-defined game=");
        System.out.println("===============================================");

        // Attention: Arrays.asList cannot remove the selected element
        java.util.List<String> teams = new ArrayList<>(Arrays.asList("Real Madrid", "Barcelona", "Manchester United",
            "Chelsea", "Juventus", "AC Milan"));
        java.util.List<String> stadiums = new ArrayList<>(Arrays.asList("San Siro", "Nou Camp", "Bernabeu", "Old Trafford"));

        Scanner scanner = new Scanner(System.in);
        boolean started = false;
        String homeTeamName = null, visitingTeamName = null;
        final String prefix = " -> ";
        while (scanner.hasNextLine()) {
            // Status transition will be introduced later
            // Or using continue to control flow
            String input = scanner.nextLine().trim();
            setPromptFormat(cp);

            int currentIndex;
            if ("Default".equalsIgnoreCase(input) && !started) {
                board.applyDefaultSettings();
                scanner.close();
                break;
            } else if ("Start".equalsIgnoreCase(input) && !started) {
                cp.println(prefix + "Here we go! Please select home team first:");
                cp.println(prefix + join(teams));
                started = true;
            } else if (started && homeTeamName == null && (currentIndex = indexOf(input, teams)) != -1) {
                board.setHomeTeam(Team.fromJSON("real-madrid.json"));

                homeTeamName = teams.get(currentIndex);
                cp.println(prefix + "Great! You have selected " + homeTeamName + " as the home team.");
                cp.println(prefix + "Please select the visiting team then:");

                // Avoid home team and visiting team are same
                teams.remove(currentIndex);
                cp.println(prefix + join(teams));
            } else if (started && homeTeamName != null && visitingTeamName == null &&
                (currentIndex = indexOf(input, teams)) != -1) {
                visitingTeamName = teams.get(currentIndex);
                board.setVisitingTeam(Team.fromJSON("barcelona.json"));
                cp.println(prefix + "Great! You have selected " + visitingTeamName + " as visiting team.");
                cp.println(prefix + "Please select the stadium for the match:");
                cp.println(prefix + join(stadiums));
            } else if (started && homeTeamName != null && visitingTeamName != null &&
                (currentIndex = indexOf(input, stadiums)) != -1) {
                String stadium = stadiums.get(currentIndex);
                board.setField(stadium);
                cp.println(prefix + "Good job! All are settle down. Game will start in " + stadium + " Stadium soon.");
                scanner.close();
                break;
            } else {
                cp.setForegroundColor(FColor.RED);
                cp.println(prefix + "OOPS! Dude I can't understand what you are looking for.\uD83D\uDE02");
            }

            cp.clear();
        }

        cp.clear();
        board.initialize().initLocations();
        initGame(board);
    }

    private static void setPromptFormat(ColoredPrinter cp) {
        cp.setForegroundColor(FColor.GREEN);
        cp.setBackgroundColor(BColor.BLACK);
    }

    private static <T> String join(java.util.List<T> list) {
        String s = list.toString();
        return s.substring(1, s.length() - 1);
    }

    /**
     * Support whole match, or first letter match, or access via index directly
     * For example: Real Madrid, R or 1 will both return 0(index is 0 based)
     * Enhance this as it's required by assignment specification
     */
    private static int indexOf(String input, java.util.List<String> elements) {
        if (input.matches("[0-9]")) {
            int number = Integer.valueOf(input);
            return number <= elements.size() ? number - 1 : -1;
        } else {
            if (input.length() == 0) return -1;

            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).equalsIgnoreCase(input) ||
                    elements.get(i).toUpperCase().startsWith(input.toUpperCase())) {
                    return i;
                }
            }

            return -1;
        }
    }

    private static MediaPlayer BG_PLAYER;

    private static void initGame(GameBoard board) {
        BG_PLAYER = Tools.playAudio("UEFA-Champions-League.mp3");

        Tools.setTheme();
        Game game = new Game(board);
        game.setVisible(true);

        while (!board.isRunning()) {
            BG_PLAYER.play();
        }
    }

}
