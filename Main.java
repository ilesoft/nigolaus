import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        String host = args[0];
        int port= 70;
        Core core = new Core();
        core.stack = new ArrayList<View>();
        core.emptyView = false;


        core.request(new MenuLine(ItemType.MENU, "Title", "", host, port));
        View view = core.getCurrentView();
        if (view.type == ItemType.MENU)
        {
            for (int i = 0; i < ((Menu)view).lines.size(); i++)
            {
                MenuLine line = ((Menu)view).lines.get(i);

                switch (line.type)
                {
                    case INFO:
                    {
                        System.out.println(line.userDisplayString);
                        break;
                    }
                    case MENU:
                    {
                        System.out.println("🗀 " + line.userDisplayString);
                    }
                }
            }
        }
    }
}
