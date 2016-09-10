package p455w0rd.p455w0rdsthings.lib.asm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ASMReader
{
  public static Map<String, Integer> opCodes = new HashMap<String, Integer>();
  public static byte[] TYPE;
  
  static
  {
    opCodes.put("NOP", Integer.valueOf(0));
    opCodes.put("ACONST_NULL", Integer.valueOf(1));
    opCodes.put("ICONST_M1", Integer.valueOf(2));
    opCodes.put("ICONST_0", Integer.valueOf(3));
    opCodes.put("ICONST_1", Integer.valueOf(4));
    opCodes.put("ICONST_2", Integer.valueOf(5));
    opCodes.put("ICONST_3", Integer.valueOf(6));
    opCodes.put("ICONST_4", Integer.valueOf(7));
    opCodes.put("ICONST_5", Integer.valueOf(8));
    opCodes.put("LCONST_0", Integer.valueOf(9));
    opCodes.put("LCONST_1", Integer.valueOf(10));
    opCodes.put("FCONST_0", Integer.valueOf(11));
    opCodes.put("FCONST_1", Integer.valueOf(12));
    opCodes.put("FCONST_2", Integer.valueOf(13));
    opCodes.put("DCONST_0", Integer.valueOf(14));
    opCodes.put("DCONST_1", Integer.valueOf(15));
    opCodes.put("BIPUSH", Integer.valueOf(16));
    opCodes.put("SIPUSH", Integer.valueOf(17));
    opCodes.put("LDC", Integer.valueOf(18));
    opCodes.put("ILOAD", Integer.valueOf(21));
    opCodes.put("LLOAD", Integer.valueOf(22));
    opCodes.put("FLOAD", Integer.valueOf(23));
    opCodes.put("DLOAD", Integer.valueOf(24));
    opCodes.put("ALOAD", Integer.valueOf(25));
    opCodes.put("IALOAD", Integer.valueOf(46));
    opCodes.put("LALOAD", Integer.valueOf(47));
    opCodes.put("FALOAD", Integer.valueOf(48));
    opCodes.put("DALOAD", Integer.valueOf(49));
    opCodes.put("AALOAD", Integer.valueOf(50));
    opCodes.put("BALOAD", Integer.valueOf(51));
    opCodes.put("CALOAD", Integer.valueOf(52));
    opCodes.put("SALOAD", Integer.valueOf(53));
    opCodes.put("ISTORE", Integer.valueOf(54));
    opCodes.put("LSTORE", Integer.valueOf(55));
    opCodes.put("FSTORE", Integer.valueOf(56));
    opCodes.put("DSTORE", Integer.valueOf(57));
    opCodes.put("ASTORE", Integer.valueOf(58));
    opCodes.put("IASTORE", Integer.valueOf(79));
    opCodes.put("LASTORE", Integer.valueOf(80));
    opCodes.put("FASTORE", Integer.valueOf(81));
    opCodes.put("DASTORE", Integer.valueOf(82));
    opCodes.put("AASTORE", Integer.valueOf(83));
    opCodes.put("BASTORE", Integer.valueOf(84));
    opCodes.put("CASTORE", Integer.valueOf(85));
    opCodes.put("SASTORE", Integer.valueOf(86));
    opCodes.put("POP", Integer.valueOf(87));
    opCodes.put("POP2", Integer.valueOf(88));
    opCodes.put("DUP", Integer.valueOf(89));
    opCodes.put("DUP_X1", Integer.valueOf(90));
    opCodes.put("DUP_X2", Integer.valueOf(91));
    opCodes.put("DUP2", Integer.valueOf(92));
    opCodes.put("DUP2_X1", Integer.valueOf(93));
    opCodes.put("DUP2_X2", Integer.valueOf(94));
    opCodes.put("SWAP", Integer.valueOf(95));
    opCodes.put("IADD", Integer.valueOf(96));
    opCodes.put("LADD", Integer.valueOf(97));
    opCodes.put("FADD", Integer.valueOf(98));
    opCodes.put("DADD", Integer.valueOf(99));
    opCodes.put("ISUB", Integer.valueOf(100));
    opCodes.put("LSUB", Integer.valueOf(101));
    opCodes.put("FSUB", Integer.valueOf(102));
    opCodes.put("DSUB", Integer.valueOf(103));
    opCodes.put("IMUL", Integer.valueOf(104));
    opCodes.put("LMUL", Integer.valueOf(105));
    opCodes.put("FMUL", Integer.valueOf(106));
    opCodes.put("DMUL", Integer.valueOf(107));
    opCodes.put("IDIV", Integer.valueOf(108));
    opCodes.put("LDIV", Integer.valueOf(109));
    opCodes.put("FDIV", Integer.valueOf(110));
    opCodes.put("DDIV", Integer.valueOf(111));
    opCodes.put("IREM", Integer.valueOf(112));
    opCodes.put("LREM", Integer.valueOf(113));
    opCodes.put("FREM", Integer.valueOf(114));
    opCodes.put("DREM", Integer.valueOf(115));
    opCodes.put("INEG", Integer.valueOf(116));
    opCodes.put("LNEG", Integer.valueOf(117));
    opCodes.put("FNEG", Integer.valueOf(118));
    opCodes.put("DNEG", Integer.valueOf(119));
    opCodes.put("ISHL", Integer.valueOf(120));
    opCodes.put("LSHL", Integer.valueOf(121));
    opCodes.put("ISHR", Integer.valueOf(122));
    opCodes.put("LSHR", Integer.valueOf(123));
    opCodes.put("IUSHR", Integer.valueOf(124));
    opCodes.put("LUSHR", Integer.valueOf(125));
    opCodes.put("IAND", Integer.valueOf(126));
    opCodes.put("LAND", Integer.valueOf(127));
    opCodes.put("IOR", Integer.valueOf(128));
    opCodes.put("LOR", Integer.valueOf(129));
    opCodes.put("IXOR", Integer.valueOf(130));
    opCodes.put("LXOR", Integer.valueOf(131));
    opCodes.put("IINC", Integer.valueOf(132));
    opCodes.put("I2L", Integer.valueOf(133));
    opCodes.put("I2F", Integer.valueOf(134));
    opCodes.put("I2D", Integer.valueOf(135));
    opCodes.put("L2I", Integer.valueOf(136));
    opCodes.put("L2F", Integer.valueOf(137));
    opCodes.put("L2D", Integer.valueOf(138));
    opCodes.put("F2I", Integer.valueOf(139));
    opCodes.put("F2L", Integer.valueOf(140));
    opCodes.put("F2D", Integer.valueOf(141));
    opCodes.put("D2I", Integer.valueOf(142));
    opCodes.put("D2L", Integer.valueOf(143));
    opCodes.put("D2F", Integer.valueOf(144));
    opCodes.put("I2B", Integer.valueOf(145));
    opCodes.put("I2C", Integer.valueOf(146));
    opCodes.put("I2S", Integer.valueOf(147));
    opCodes.put("LCMP", Integer.valueOf(148));
    opCodes.put("FCMPL", Integer.valueOf(149));
    opCodes.put("FCMPG", Integer.valueOf(150));
    opCodes.put("DCMPL", Integer.valueOf(151));
    opCodes.put("DCMPG", Integer.valueOf(152));
    opCodes.put("IFEQ", Integer.valueOf(153));
    opCodes.put("IFNE", Integer.valueOf(154));
    opCodes.put("IFLT", Integer.valueOf(155));
    opCodes.put("IFGE", Integer.valueOf(156));
    opCodes.put("IFGT", Integer.valueOf(157));
    opCodes.put("IFLE", Integer.valueOf(158));
    opCodes.put("IF_ICMPEQ", Integer.valueOf(159));
    opCodes.put("IF_ICMPNE", Integer.valueOf(160));
    opCodes.put("IF_ICMPLT", Integer.valueOf(161));
    opCodes.put("IF_ICMPGE", Integer.valueOf(162));
    opCodes.put("IF_ICMPGT", Integer.valueOf(163));
    opCodes.put("IF_ICMPLE", Integer.valueOf(164));
    opCodes.put("IF_ACMPEQ", Integer.valueOf(165));
    opCodes.put("IF_ACMPNE", Integer.valueOf(166));
    opCodes.put("GOTO", Integer.valueOf(167));
    opCodes.put("JSR", Integer.valueOf(168));
    opCodes.put("RET", Integer.valueOf(169));
    opCodes.put("TABLESWITCH", Integer.valueOf(170));
    opCodes.put("LOOKUPSWITCH", Integer.valueOf(171));
    opCodes.put("IRETURN", Integer.valueOf(172));
    opCodes.put("LRETURN", Integer.valueOf(173));
    opCodes.put("FRETURN", Integer.valueOf(174));
    opCodes.put("DRETURN", Integer.valueOf(175));
    opCodes.put("ARETURN", Integer.valueOf(176));
    opCodes.put("RETURN", Integer.valueOf(177));
    opCodes.put("GETSTATIC", Integer.valueOf(178));
    opCodes.put("PUTSTATIC", Integer.valueOf(179));
    opCodes.put("GETFIELD", Integer.valueOf(180));
    opCodes.put("PUTFIELD", Integer.valueOf(181));
    opCodes.put("INVOKEVIRTUAL", Integer.valueOf(182));
    opCodes.put("INVOKESPECIAL", Integer.valueOf(183));
    opCodes.put("INVOKESTATIC", Integer.valueOf(184));
    opCodes.put("INVOKEINTERFACE", Integer.valueOf(185));
    opCodes.put("INVOKEDYNAMIC", Integer.valueOf(186));
    opCodes.put("NEW", Integer.valueOf(187));
    opCodes.put("NEWARRAY", Integer.valueOf(188));
    opCodes.put("ANEWARRAY", Integer.valueOf(189));
    opCodes.put("ARRAYLENGTH", Integer.valueOf(190));
    opCodes.put("ATHROW", Integer.valueOf(191));
    opCodes.put("CHECKCAST", Integer.valueOf(192));
    opCodes.put("INSTANCEOF", Integer.valueOf(193));
    opCodes.put("MONITORENTER", Integer.valueOf(194));
    opCodes.put("MONITOREXIT", Integer.valueOf(195));
    opCodes.put("MULTIANEWARRAY", Integer.valueOf(197));
    opCodes.put("IFNULL", Integer.valueOf(198));
    opCodes.put("IFNONNULL", Integer.valueOf(199));
    
    TYPE = new byte[200];
    String s = "AAAAAAAAAAAAAAAABBJ__CCCCC____________________AAAAAAAACCCCC____________________AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHCLMAAAAAAEEEEFFFFGDBDAADDAA_NHH";
    for (int i = 0; i < s.length(); i++) {
      TYPE[i] = ((byte)(s.charAt(i) - 'A'));
    }
  }
  
  public static Map<String, ASMBlock> loadResource(String res)
  {
    return loadResource(ASMHelper.class.getResourceAsStream(res), res);
  }
  
  public static Map<String, ASMBlock> loadResource(InputStream in, String res)
  {
    HashMap<String, ASMBlock> blocks = new HashMap<String, ASMBlock>();
    String current = "unnamed";
    ASMBlock block = new ASMBlock();
    try
    {
      BufferedReader r = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = r.readLine()) != null)
      {
        int hpos = line.indexOf('#');
        if (hpos >= 0) {
          line = line.substring(0, hpos);
        }
        line = line.trim();
        if (line.length() != 0) {
          if (line.startsWith("list "))
          {
            if (block.list.size() > 0) {
              blocks.put(current, block);
            }
            current = line.substring(5);
            block = new ASMBlock();
          }
          else
          {
            try
            {
              AbstractInsnNode insn = null;
              String[] split = line.replace(" : ", ":").split(" ");
              Integer i_opcode = (Integer)opCodes.get(split[0]);
              if (i_opcode == null)
              {
                if (split[0].equals("LINENUMBER")) {
                  insn = new LineNumberNode(Integer.parseInt(split[1]), block.getOrAdd(split[2]));
                } else if (split[0].startsWith("L")) {
                  insn = block.getOrAdd(split[0]);
                } else {
                  throw new Exception("Unknown opcode " + split[0]);
                }
              }
              else
              {
                int opcode = i_opcode.intValue();
                switch (TYPE[opcode])
                {
                case 0: 
                  insn = new InsnNode(opcode);
                  break;
                case 1: 
                  insn = new IntInsnNode(opcode, Integer.parseInt(split[1]));
                  break;
                case 2: 
                  insn = new VarInsnNode(opcode, Integer.parseInt(split[1]));
                  break;
                case 3: 
                  insn = new ObfMapping(split[1]).toClassloading().toInsn(opcode);
                  break;
                case 4: 
                case 5: 
                  StringBuilder sb = new StringBuilder();
                  for (int i = 1; i < split.length; i++) {
                    sb.append(split[i]);
                  }
                  insn = ObfMapping.fromDesc(sb.toString()).toClassloading().toInsn(opcode);
                  break;
                case 6: 
                  throw new Exception("Found INVOKEDYNAMIC while reading");
                case 7: 
                  insn = new JumpInsnNode(opcode, block.getOrAdd(split[1]));
                  break;
                case 9: 
                  String cst = split[1];
                  if (cst.equals("*")) {
                    insn = new LdcInsnNode(null);
                  } else if (cst.endsWith("\"")) {
                    insn = new LdcInsnNode(cst.substring(1, cst.length() - 1));
                  } else if (cst.endsWith("L")) {
                    insn = new LdcInsnNode(Long.valueOf(cst.substring(0, cst.length() - 1)));
                  } else if (cst.endsWith("F")) {
                    insn = new LdcInsnNode(Float.valueOf(cst.substring(0, cst.length() - 1)));
                  } else if (cst.endsWith("D")) {
                    insn = new LdcInsnNode(Double.valueOf(cst.substring(0, cst.length() - 1)));
                  } else if (cst.contains(".")) {
                    insn = new LdcInsnNode(Double.valueOf(cst));
                  } else {
                    insn = new LdcInsnNode(Integer.valueOf(cst));
                  }
                  break;
                case 10: 
                  insn = new IincInsnNode(opcode, Integer.parseInt(split[1]));
                  break;
                case 8: 
                  throw new Exception("Use L# for labels");
                case 11: 
                case 12: 
                  throw new Exception("I don't know how to deal with this insn type");
                case 13: 
                  insn = new MultiANewArrayInsnNode(split[1], Integer.parseInt(split[2]));
                  break;
                case 14: 
                  throw new Exception("Use ClassWriter.COMPUTE_FRAMES");
                }
              }
              if (insn != null) {
                block.list.add(insn);
              }
            }
            catch (Exception e)
            {
              System.err.println("Error while reading ASM Block " + current + " from " + res + ", line: " + line);
              
              e.printStackTrace();
            }
          }
        }
      }
      r.close();
      if (block.list.size() > 0) {
        blocks.put(current, block);
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException("Failed to read ASM resource: " + res, e);
    }
    return blocks;
  }
}


