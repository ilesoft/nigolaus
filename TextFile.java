public class TextFile extends View
{
    String text;
    public TextFile(String text, String userDisplayString, String selector, String host, int port)
    {
        this.type = ItemType.TEXTFILE;
        this.text = text;
        this.userDisplayString = userDisplayString;
        this.selector = selector;
        this.host = host;
        this.port = port;
    }

}
