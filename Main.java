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
            for (int i = 0; i < ((Menu)view).lines.size() - 1; i++)
            {
                System.out.println(((Menu)view).lines.get(i).userDisplayString);
            }
        }
    }
}
