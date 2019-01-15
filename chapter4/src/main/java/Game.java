import com.sun.javafx.application.PlatformImpl;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
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

    private void transparent(JButton... buttons) {
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
        this.transparent(startBtn, resetBtn, exitBtn);

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
        final GameBoard board = new GameBoard();
        // If splash has been set via VM options, skip text mode
        // java -splash:images/champions.jpg Game
        if (SplashScreen.getSplashScreen() != null ||
            args.length > 0 && args[0].equalsIgnoreCase("Start")) {
            board.applyDefaultSettings();
            initGame(board);
            return;
        }

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

        java.util.List<String> teams = Arrays.asList("Real Madrid", "Barcelona", "Manchester United",
            "Chelsea", "Juventus", "AC Milan");
        java.util.List<String> stadiums = Arrays.asList("San Siro", "Nou Camp", "Bernabeu", "Old Trafford");

        Scanner scanner = new Scanner(System.in);
        boolean started = false;
        String homeTeamName = null, visitingTeamName = null;
        while (scanner.hasNextLine()) {
            // Status transition will be introduced later
            String input = scanner.nextLine().trim();
            if ("Default".equalsIgnoreCase(input) && !started) {
                board.applyDefaultSettings();
                scanner.close();
                break;
            } else if ("Start".equalsIgnoreCase(input) && !started) {
                System.out.println("Here we go! Please select home team first:");
                System.out.println(teams);
                started = true;
            } else if (started && homeTeamName == null && teams.indexOf(input) != -1) {
                board.setHomeTeam(Team.fromJSON("real-madrid.json"));
                homeTeamName = input;
                System.out.println("Great! You have selected " + homeTeamName + " as the home team.");
                System.out.println("Please select the visiting team then:");
                System.out.println(teams);
            } else if (started && homeTeamName != null && visitingTeamName == null && teams.indexOf(input) != -1 &&
                !input.equals(homeTeamName)) {
                visitingTeamName = input;
                board.setVisitingTeam(Team.fromJSON("barcelona.json"));
                System.out.println("Great! You have selected " + visitingTeamName + " as visiting team.");
                System.out.println("Please select the stadium for the match:\n" + stadiums);
            } else if (started && homeTeamName != null && visitingTeamName != null && stadiums.indexOf(input) != -1) {
                board.setField(input);
                System.out.println("Good job! All are settle down. Game will initGame in " + input + " Stadium soon.");
                scanner.close();
                break;
            } else {
                System.out.println("OOPS! Dude I can't understand what you are looking for.\uD83D\uDE02");
            }
        }

        board.initialize().initLocations();
        initGame(board);
    }

    private static MediaPlayer BG_PLAYER;

    private static void initGame(GameBoard board) {
        PlatformImpl.startup(() -> {});
        BG_PLAYER = Tools.playAudio("UEFA-Champions-League.mp3");

        Tools.setTheme();
        Game game = new Game(board);
        game.setVisible(true);

        while (!board.isRunning()) {
            BG_PLAYER.play();
        }
    }

}
