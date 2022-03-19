import java.util.ArrayList;

public class Menu extends View
{
    ArrayList<MenuLine> lines;

    public Menu(String userDisplayString, String selector, String host, int port)
    {
        this.type = ItemType.MENU;
        this.userDisplayString = userDisplayString;
        this.selector = selector;
        this.host = host;
        this.port = port;

        this.lines = new ArrayList<MenuLine>();
    }

    public void addItem(MenuLine line)
    {
        this.lines.add(line);
    }
}
