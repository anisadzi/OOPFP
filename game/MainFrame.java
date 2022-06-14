package game;

//importing methods and utilities
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

//the main frame using swing
public class MainFrame extends javax.swing.JFrame implements ActionListener {
    //the main frame includes:
    public MainFrame() {
        initComponents();
        initIcons();
        initGame();
    }

    //game initialization
    private void initGame() {
        score = 0;
        int x = 0;
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(icons[x], new ImageIcon(getClass().getResource("/images/logo.png")));
            tiles[i].addActionListener(this);
            gamePanel.add(tiles[i]);
            if ((i + 1) % 2 == 0) {
                x++;
            }
        }
        title.setText("Score: " + score);
        shuffle();

    }

    //initializing(getting) the icon/picture showed on tiles
    private void initIcons() {
        Image img;
        for (int i = 0; i < icons.length; i++) {
            img = new ImageIcon(getClass().getResource("/images/card" + i + ".png")).getImage();
            icons[i] = createIcon(img);
        }

    }

    //creating the icon from the image that has been taken
    private ImageIcon createIcon(Image img) {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        bi.createGraphics().drawImage(img, 0, 0, null);
        img = bi.getScaledInstance(80, 80, 1);
        return new ImageIcon(img);
    }

    private void showHelp() {
        //show help or hint, so everytime you look at hint the score -50
        //if tiles[0] would be null than all the tiles would be null here
        if (tiles[0] != null) {
            for (int i = 0; i < tiles.length; i++) {
                if (!tiles[i].isNoIcon()) {
                    tiles[i].showTile();
                    tiles[i].removeActionListener(this);
                }
            }
            score -= 50;
            title.setText("Score: " + score);
        }
    }

    //hiding(folding) the help/hint on its own
    private void hideHelp() {

        for (int i = 0; i < tiles.length; i++) {
            if (!tiles[i].isNoIcon()) {
                tiles[i].hideTile();
                tiles[i].addActionListener(this);
            }
        }

    }

    //check whether the user win or not
    private void check() {

        if (predict1 != predict2 && predict1.getImage() == predict2.getImage()) {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 3; i++) {
                        try {
                            predict1.hideTile();
                            predict2.hideTile();
                            Thread.sleep(100);
                            predict1.showTile();
                            predict2.showTile();
                            Thread.sleep(100);
                        }
                        catch (InterruptedException ex) {
                            System.out.println(ex);
                        }
                    }
                    predict1.setNoIcon();
                    predict2.setNoIcon();
                    for (int i = 0; i < tiles.length; i++) {
                        if (!tiles[i].isNoIcon()) {
                            won = false;
                            break;
                        }

                        else {
                            won = true;
                        }
                    }
                    if (won) {
                        if (score > 0) {
                            JOptionPane.showMessageDialog(gamePanel, "You Won! Your Score is " + score);
                        }

                        else {
                            JOptionPane.showMessageDialog(gamePanel, "You Loose! Your Score is " + score);
                        }
                        initGame();
                    }
                }
            }.start();
            predict1.removeActionListener(this);
            predict2.removeActionListener(this);
            score += 100;
            title.setText("Score: " + score);

        } else {//if user is wrong
            predict1.hideTile();
            predict2.hideTile();
            score -= 10;
            title.setText("Score: " + score);
        }
    }

    //shuffling/randomizing the tiles
    private void shuffle() {
        gamePanel.removeAll();
        ArrayList<Integer> al = new ArrayList<Integer>();
        for (int i = 0; i < 36;) {
            int x = (int) (Math.random() * 36);
            if (!al.contains(x)) {
                al.add(x);
                i++;
            }
        }
        for (int i = 0; i < 36; i++) {
            gamePanel.add(tiles[al.get(i)]);
            tiles[al.get(i)].hideTile();
        }
    }

    //initializing the components of the frame
    private void initComponents() {

        titlePanel = new javax.swing.JPanel();
        title = new javax.swing.JTextField();
        close = new javax.swing.JLabel();
        help = new javax.swing.JLabel();
        gamePanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        play = new javax.swing.JButton();
        load = new javax.swing.JButton();

        //getting the name for the main frame, colors, and the logo
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fruit Memory Game");
        setBackground(new java.awt.Color(150, 175, 175));
        setIconImage(new ImageIcon(getClass().getResource("/images/logo.png")).getImage());
        setLocationByPlatform(true);
        setName("MainFrame");
        setUndecorated(true);

        //background and dimension for the title bar/panel
        titlePanel.setBackground(new java.awt.Color(0, 0, 0));
        titlePanel.setPreferredSize(new java.awt.Dimension(300, 25));
        titlePanel.setLayout(new java.awt.BorderLayout());

        //setting the title bar/panel (font and color, placing)
        title.setEditable(false);
        title.setBackground(new java.awt.Color(0, 0, 0));
        title.setFont(new java.awt.Font("Tahoma", 1, 11));
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        title.setText("Score: ");
        title.setToolTipText("After Clicking mouse here use arrow keys to move");
        title.setBorder(null);
        title.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        title.setSelectionColor(new java.awt.Color(0, 0, 0));
        title.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                titleMouseDragged(evt);
            }
        });
        title.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                titleKeyPressed(evt);
            }
        });
        titlePanel.add(title, java.awt.BorderLayout.CENTER);

        //the X button to close bar
        close.setBackground(new java.awt.Color(0, 0, 0));
        close.setFont(new java.awt.Font("Tahoma", 1, 14));
        close.setForeground(new java.awt.Color(255, 255, 255));
        close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close.setText("X");
        close.setToolTipText("Close");
        close.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        close.setPreferredSize(new java.awt.Dimension(25, 25));
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeMouseClicked(evt);
            }
        });
        titlePanel.add(close, java.awt.BorderLayout.LINE_END);

        //the help option at the bar
        help.setFont(new java.awt.Font("Tahoma", 1, 18));
        help.setForeground(new java.awt.Color(255, 255, 255));
        help.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        help.setText("?");
        help.setToolTipText("Right click to hide controls and Left click to see Images");
        help.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        help.setPreferredSize(new java.awt.Dimension(25, 25));
        help.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                helpMouseClicked(evt);
            }
        });
        titlePanel.add(help, java.awt.BorderLayout.LINE_START);

        getContentPane().add(titlePanel, java.awt.BorderLayout.NORTH);

        //color for the background of the game panel
        gamePanel.setBackground(new java.awt.Color(0, 0, 0));
        gamePanel.setPreferredSize(new java.awt.Dimension(630, 630));
        gamePanel.setLayout(new java.awt.GridLayout(6, 6, 5, 5));
        getContentPane().add(gamePanel, java.awt.BorderLayout.CENTER);

        //color for control panel
        controlPanel.setBackground(new java.awt.Color(0, 0, 0));
        controlPanel.setPreferredSize(new java.awt.Dimension(300, 40));
        controlPanel.setLayout(new java.awt.GridLayout(1, 2));

        //color and font for the play button
        play.setBackground(new java.awt.Color(255, 255, 255));
        play.setFont(new java.awt.Font("Tahoma", 0, 18));
        play.setForeground(new java.awt.Color(153, 153, 255));
        play.setText("PLAY");
        play.setToolTipText("Play new Game");
        play.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playActionPerformed(evt);
            }
        });
        controlPanel.add(play);

        //color and font for the load button
        load.setBackground(new java.awt.Color(255, 255, 255));
        load.setFont(new java.awt.Font("Tahoma", 0, 18));
        load.setForeground(new java.awt.Color(153, 153, 255));
        load.setText("LOAD");
        load.setToolTipText("Load your favourite images");
        load.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });
        controlPanel.add(load);

        getContentPane().add(controlPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }

    //if mouse click close then it will close
    private void closeMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON1) {
            this.dispose();
        }
    }

    //if mouse click help it will show help
    private void helpMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON1) {
            if (!helping) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            helping = true;
                            showHelp();
                            Thread.sleep(10000); //time the hint will show
                            hideHelp();
                            helping = false;
                        }
                        catch (InterruptedException ex) {
                            System.out.println(ex);
                        }
                    }
                }.start();
            }
        }
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (controlPanel.isVisible()) {
                setSize(600, 625);
                controlPanel.setVisible(false);
            }
            else {
                setSize(600, 665);
                controlPanel.setVisible(true);
            }
        }
    }

    //what would happen if the title is pressed
    private void titleKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            setLocation(getX() - 5, getY());
        }
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            setLocation(getX() + 5, getY());
        }
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            setLocation(getX(), getY() - 5);
        }
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            setLocation(getX(), getY() + 5);
        }
    }

    //what would happen if the LOAD button is clicked
    private void loadActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int response = chooser.showOpenDialog(predict1);
        if (response == JFileChooser.APPROVE_OPTION) {
            File[] file = chooser.getSelectedFiles();
            if (file.length >= 18) {
                for (int i = 0; i < 18; i++) {
                    icons[i] = createIcon(new ImageIcon(file[i].toString()).getImage());
                }
                initGame();
            }
            else {
                JOptionPane.showMessageDialog(gamePanel, "Please select 18 Files !");
            }
        }
    }

    //what would happen if the PLAY button is clicked
    private void playActionPerformed(java.awt.event.ActionEvent evt) {
        initGame();
    }

    //what would happen if the title gets dragged
    private void titleMouseDragged(java.awt.event.MouseEvent evt) {
        setLocation(evt.getXOnScreen() - 300, evt.getYOnScreen());
    }

    //calling for the app
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }


    Tile[] tiles = new Tile[36]; //how many tiles in a game
    ImageIcon[] icons = new ImageIcon[18]; //how many image needed
    int status, score;
    Tile predict1, predict2;
    private boolean won, helping;

    //declaring the variable on main frame
    private javax.swing.JLabel close;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JLabel help;
    private javax.swing.JButton load;
    private javax.swing.JButton play;
    private javax.swing.JTextField title;
    private javax.swing.JPanel titlePanel;


    @Override
    //if a tile gets clicked, what would happen
    public void actionPerformed(ActionEvent e) {
        if (status == 0) {
            predict1 = (Tile) e.getSource();
            predict1.showTile();
            status++;
        }
        else if (status == 1) {
            status++;
            predict2 = (Tile) e.getSource();
            new Thread() {
                @Override
                public void run() {
                    try {
                        predict2.showTile();
                        Thread.sleep(500);
                        check();
                        Thread.sleep(600);
                        status = 0;
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }.start();

        }
    }
}
