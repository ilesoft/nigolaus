import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        String host = args[0];
        String selector = args.length > 1 ? args[1] : "";
        int port= 70;
        Core core = new Core();
        core.stack = new ArrayList<View>();
        core.emptyView = false;

        // // Test retrieving gopher menus
        // core.request(new MenuLine(ItemType.MENU, "Title", selector, host, port));
        // View view = core.getCurrentView();
        // if (view.type == ItemType.MENU)
        // {
        //     for (int i = 0; i < ((Menu)view).lines.size(); i++)
        //     {
        //         MenuLine line = ((Menu)view).lines.get(i);

        //         switch (line.type)
        //         {
        //             case INFO:
        //             {
        //                 System.out.println(line.userDisplayString);
        //                 break;
        //             }
        //             case MENU:
        //             {
        //                 System.out.println("📁" + line.userDisplayString + "  (" + line.selector + ")");
        //                 break;
        //             }
        //             case TEXTFILE:
        //             {
        //                 System.out.println(line.userDisplayString + "  (" + line.selector + ")");
        //                 break;
        //             }
        //         }
        //     }
        // }

        // Test retrieving text documents
        core.request(new MenuLine(ItemType.TEXTFILE, "Title", "comp.sec.300/structure.md", host, port));
        View view = core.getCurrentView();
        if (view.type == ItemType.TEXTFILE)
        {
            System.out.println(((TextFile)view).text);
        }
    }
}
