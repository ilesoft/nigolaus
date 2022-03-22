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

                    if (line.length() == 0)
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

                    switch (line.charAt(0))
                    {
                        case 'i':
                        {
                            // Strip type annotation
                            line = line.substring(1, line.length());

                            if (line.indexOf(9) != -1)  // Unicode 9 is TAB character
                            {
                                line = line.substring(0, line.indexOf(9));
                            }

                            MenuLine newMenuLine = new MenuLine(ItemType.INFO , line, "", "", -1);
                            menu.addItem(newMenuLine);
                            break;
                        }
                        case '0':
                        {
                            // This is textfile

                            // Strip type annotation
                            line = line.substring(1, line.length());

                            String[] parts = line.split("\t");

                            MenuLine newMenuLine = new MenuLine(ItemType.TEXTFILE,
                                                                parts[0],
                                                                parts.length > 1 ? parts[1] : "",
                                                                parts.length > 2 ? parts[2] : "",
                                                                parts.length > 3 ? Integer.parseInt(parts[3]) : 70);
                            // TODO: Think waht to do when len parts < 4

                            menu.addItem(newMenuLine);
                            break;
                        }
                        case '1':
                        {
                            // Strip type annotation
                            line = line.substring(1, line.length());

                            String[] parts = line.split("\t");

                            MenuLine newMenuLine = new MenuLine(ItemType.MENU,
                                                                parts[0],
                                                                parts.length > 1 ? parts[1] : "",
                                                                parts.length > 2 ? parts[2] : "",
                                                                parts.length > 3 ? Integer.parseInt(parts[3]) : 70);
                            // TODO: Think waht to do when len parts < 4

                            menu.addItem(newMenuLine);
                            break;
                        }
                        case '2':
                        {
                            // CSO phonebook
                            MenuLine newMenuLine = new MenuLine(ItemType.CSO, line, "", "", -1);
                            break;
                        }
                        case '3':
                        {
                            // error
                            break;
                        }
                        case '4':
                        {
                            // BinHexed Macintosh file
                            MenuLine newMenuLine = new MenuLine(ItemType.MAC, line, "", "", -1);
                            break;
                        }
                        case '5':
                        {
                            // DOS binary archive of some sort
                            MenuLine newMenuLine = new MenuLine(ItemType.DOSARCHIVE, line, "", "", -1);
                            break;
                        }
                        case '6':
                        {
                            // UNIX uuencoded file
                            MenuLine newMenuLine = new MenuLine(ItemType.UNIXUUENC, line, "", "", -1);
                            break;
                        }
                        case '7':
                        {
                            // index-search server 
                            MenuLine newMenuLine = new MenuLine(ItemType.SEARCH, line, "", "", -1);
                            break;
                        }
                        case '8':
                        {
                            // points to a text-based telnet session
                            MenuLine newMenuLine = new MenuLine(ItemType.TELNET, line, "", "", -1);
                            break;
                        }
                        case '9':
                        {
                            // binary file
                            MenuLine newMenuLine = new MenuLine(ItemType.BIN, line, "", "", -1);
                            break;
                        }
                        case '+':
                        {
                            // redundant server
                            MenuLine newMenuLine = new MenuLine(ItemType.REDUNDANT, line, "", "", -1);
                            break;
                        }
                        case 'T':
                        {
                            // ext-based tn3270 session
                            MenuLine newMenuLine = new MenuLine(ItemType.TN3270, line, "", "", -1);
                            break;
                        }
                        case 'g':
                        {
                            // gif
                            MenuLine newMenuLine = new MenuLine(ItemType.GIF, line, "", "", -1);
                            break;
                        }
                        case 'I':
                        {
                            // other image
                            MenuLine newMenuLine = new MenuLine(ItemType.IMAGE, line, "", "", -1);
                            break;
                        }
                    }
                }

                if (!this.reseted)
                {
                    this.requestInProgres = false;
                    this.stack.add(menu);
                }
            }

            if (menuLine.type == ItemType.TEXTFILE)
            {
                StringBuilder text = new StringBuilder();
                while ((line = response.readLine()) != null)
                {
                    if (line == ".")
                    {
                        response.close();
                        socket.close();
                        break;
                    }

                    text.append(line);
                    text.append('\n');
                }
                TextFile doc = new TextFile(text.toString(), menuLine.userDisplayString, menuLine.selector, menuLine.host, menuLine.port);

                if (!this.reseted)
                {
                    this.requestInProgres = false;
                    this.stack.add(doc);
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
