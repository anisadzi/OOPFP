package game;

import javax.swing.ImageIcon;
import javax.swing.JButton;

class Tile extends JButton {

    ImageIcon icon1;
    ImageIcon icon2;
    private boolean hidden, noIcon;

    //setting the tile
    public Tile(ImageIcon icon1, ImageIcon icon2) {
        this.icon1 = icon1;
        this.icon2 = icon2;
        setSize(100, 100);
        setFocusable(false);
    }

    //show tile
    public synchronized void showTile() {
        setIcon(icon1);
        hidden = false;
    }

    //hiding the tile
    public synchronized void hideTile() {
        setIcon(icon2);
        hidden = true;
    }

    //if setting no icon
    public synchronized void setNoIcon() {
        setIcon(null);
        noIcon = true;
    }

    //getting image
    public ImageIcon getImage() {
        return icon1;
    }

    //if there is no icon
    public synchronized boolean isNoIcon() {

        return noIcon;
    }

}
