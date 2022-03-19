import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Core
{
    public static void main(String[] args)
    {
        String host = args[0];
        int port= 70;
        
        Socket socket = null;
        BufferedReader response = null;

        try
        {
            socket = new Socket(host, port);
            String selector = "\r\n";
            socket.getOutputStream().write(selector.getBytes());

            response = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            String line;
            while ((line = response.readLine()) != null)
            {
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
                }

                if (line == ".")
                {
                    response.close();
                    socket.close();
                    break;
                }
                System.out.println(line);
            }

        }
        catch(Exception e)
        {
            System.out.print(e);
        }
    }
}
