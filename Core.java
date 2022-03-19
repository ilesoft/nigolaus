import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Core
{
    ArrayList<View> stack;

    Boolean requestInProgres = false;
    boolean reseted;
    boolean emptyView = true;

    public View back()
    {
        return this.stack.remove(this.stack.size() - 1);
    }

    public void reset()
    {
        if (this.requestInProgres)
        {
            this.reseted = true;
            this.requestInProgres = false;
        }
    }

    public View getCurrentView()
    {
        System.out.print(this);
        return this.stack.get(this.stack.size() - 1);
    }

    public void request(MenuLine menuLine)
    {
        this.requestInProgres = true;
        Socket socket = null;
        BufferedReader response = null;

        try
        {
            socket = new Socket(menuLine.host, menuLine.port);
            String selectorLine = menuLine.selector + "\r\n";
            socket.getOutputStream().write(selectorLine.getBytes());

            response = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            String line;

            if (menuLine.type == ItemType.MENU)
            {
                Menu menu = new Menu(menuLine.userDisplayString, menuLine.selector, menuLine.host, menuLine.port);
                while ((line = response.readLine()) != null)
                {
                    if (line == ".")
                    {
                        response.close();
                        socket.close();
                        break;
                    }

                    if (line.length() <= 0)
                    {
                        menu.error = true;
                        menu.errorText = "There is line with zero length";
                        if (this.reseted)
                        {
                            return;
                        }
                        else
                        {
                            this.requestInProgres = false;
                            this.stack.add(menu);
                            return;
                        }
                    }

                    if (line.length() > 0 && line.charAt(0) == 'i')
                    {
                        if (line.indexOf(9) != -1)  // Unicode 9 is TAB character
                        {
                            line = line.substring(1, line.indexOf(9));
                        }
                        else
                        {
                            line = line.substring(1, line.length() - 1);
                        }
                        MenuLine newMenuLine = new MenuLine(ItemType.INFO , line, "", "", -1);
                        menu.addItem(newMenuLine);
                    }
                    else
                    {
                        MenuLine newMenuLine = new MenuLine(ItemType.MENU, line, "selector", "tcpbin.org", 4242);
                        menu.addItem(newMenuLine);
                        if (!this.reseted)
                        {
                            this.requestInProgres = false;
                            this.stack.add(menu);
                        }
                    }
                }

            }
        }
        catch(Exception e)
        {
            this.requestInProgres = false;
            System.out.print(e);
        }
        
    }
}
