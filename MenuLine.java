public class MenuLine
{
    ItemType type;
    String userDisplayString;
    String selector;
    String host;
    int port;

    public MenuLine(ItemType type, String userDisplayString, String selector, String host, int port)
    {
        this.type = type;
        this.userDisplayString = userDisplayString;
        this.selector = selector;
        this.host = host;
        this.port = port;
    }
}

