package p455w0rd.p455w0rdsthings.lib.tool;

import p455w0rd.p455w0rdsthings.lib.tool.module.ModuleQBConverter;

public class ToolMain
{
  public static Module[] modules = { new ModuleQBConverter() };
  
  private static void printHelp()
  {
    System.out.println("Usage: [module] [args]");
    System.out.println("  Modules: ");
    for (Module m : modules) {
      System.out.println("   - " + m.name());
    }
    System.out.println("-h [module] for module help");
  }
  
  public static void main(String[] args)
  {
    if (args.length > 0)
    {
      for (Module m : modules) {
        if (args[0].equals(m.name()))
        {
          String[] args2 = new String[args.length - 1];
          System.arraycopy(args, 1, args2, 0, args2.length);
          m.main(args2);
          return;
        }
      }
      if ((args[0].equals("-h")) && (args.length >= 2)) {
        for (Module m : modules) {
          if (args[1].equals(m.name()))
          {
            m.printHelp();
            return;
          }
        }
      }
    }
    printHelp();
  }
  
  public static abstract interface Module
  {
    public abstract void main(String[] paramArrayOfString);
    
    public abstract String name();
    
    public abstract void printHelp();
  }
}


