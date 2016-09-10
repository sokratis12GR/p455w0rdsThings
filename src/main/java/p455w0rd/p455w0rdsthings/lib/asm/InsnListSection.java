package p455w0rd.p455w0rdsthings.lib.asm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class InsnListSection
  implements Iterable<AbstractInsnNode>
{
  public InsnList list;
  public int start;
  public int end;
  
  private class InsnListSectionIterator
    implements Iterator<AbstractInsnNode>
  {
    int i = 0;
    
    private InsnListSectionIterator() {}
    
    public boolean hasNext()
    {
      return this.i < InsnListSection.this.size();
    }
    
    public AbstractInsnNode next()
    {
      return InsnListSection.this.get(this.i++);
    }
    
    public void remove()
    {
      InsnListSection.this.remove(--this.i);
    }
  }
  
  public InsnListSection(InsnList list, int start, int end)
  {
    this.list = list;
    this.start = start;
    this.end = end;
  }
  
  public InsnListSection(InsnList list, AbstractInsnNode first, AbstractInsnNode last)
  {
    this(list, list.indexOf(first), list.indexOf(last) + 1);
  }
  
  public InsnListSection(InsnList list)
  {
    this(list, 0, list.size());
  }
  
  public InsnListSection()
  {
    this(new InsnList());
  }
  
  public void accept(MethodVisitor mv)
  {
    for (AbstractInsnNode insn : this) {
      insn.accept(mv);
    }
  }
  
  public AbstractInsnNode getFirst()
  {
    return size() == 0 ? null : this.list.get(this.start);
  }
  
  public AbstractInsnNode getLast()
  {
    return size() == 0 ? null : this.list.get(this.end - 1);
  }
  
  public int size()
  {
    return this.end - this.start;
  }
  
  public AbstractInsnNode get(int i)
  {
    return this.list.get(this.start + i);
  }
  
  public void set(int i, AbstractInsnNode insn)
  {
    this.list.set(get(i), insn);
  }
  
  public void remove(int i)
  {
    this.list.remove(get(i));
    this.end -= 1;
  }
  
  public void replace(AbstractInsnNode location, AbstractInsnNode insn)
  {
    this.list.set(location, insn);
  }
  
  public void add(AbstractInsnNode insn)
  {
    this.list.add(insn);
    this.end += 1;
  }
  
  public void insertBefore(InsnList insns)
  {
    int s = insns.size();
    if (this.list.size() == 0) {
      this.list.insert(insns);
    } else {
      this.list.insertBefore(this.list.get(this.start), insns);
    }
    this.start += s;
    this.end += s;
  }
  
  public void insert(InsnList insns)
  {
    if (this.end == 0) {
      this.list.insert(insns);
    } else {
      this.list.insert(this.list.get(this.end - 1), insns);
    }
  }
  
  public void replace(InsnList insns)
  {
    int s = insns.size();
    remove();
    insert(insns);
    this.end = (this.start + s);
  }
  
  public void remove()
  {
    while (this.end != this.start) {
      remove(0);
    }
  }
  
  public void setLast(AbstractInsnNode last)
  {
    this.end = (this.list.indexOf(last) + 1);
  }
  
  public void setFirst(AbstractInsnNode first)
  {
    this.start = this.list.indexOf(first);
  }
  
  public InsnListSection drop(int n)
  {
    return slice(n, size());
  }
  
  public InsnListSection take(int n)
  {
    return slice(0, n);
  }
  
  public InsnListSection slice(int start, int end)
  {
    return new InsnListSection(this.list, this.start + start, this.start + end);
  }
  
  public InsnListSection trim(Set<LabelNode> controlFlowLabels)
  {
    while ((this.start < this.end) && (!InsnComparator.insnImportant(getFirst(), controlFlowLabels))) {
      this.start += 1;
    }
    while ((this.start < this.end) && (!InsnComparator.insnImportant(getLast(), controlFlowLabels))) {
      this.end -= 1;
    }
    return this;
  }
  
  public String toString()
  {
    Textifier t = new Textifier();
    accept(new TraceMethodVisitor(t));
    StringWriter sw = new StringWriter();
    t.print(new PrintWriter(sw));
    return sw.toString();
  }
  
  public void println()
  {
    System.out.println(toString());
  }
  
  @SuppressWarnings("rawtypes")
public HashMap<LabelNode, LabelNode> identityLabelMap()
  {
    HashMap<LabelNode, LabelNode> labelMap = new HashMap<LabelNode, LabelNode>();
    for (AbstractInsnNode insn : this)
    {
      Iterator localIterator2;
      LabelNode label;
      switch (insn.getType())
      {
      case 8: 
        labelMap.put((LabelNode)insn, (LabelNode)insn);
        break;
      case 7: 
        labelMap.put(((JumpInsnNode)insn).label, ((JumpInsnNode)insn).label);
        break;
      case 12: 
        LookupSwitchInsnNode linsn = (LookupSwitchInsnNode)insn;
        labelMap.put(linsn.dflt, linsn.dflt);
        for (localIterator2 = linsn.labels.iterator(); localIterator2.hasNext();)
        {
          label = (LabelNode)localIterator2.next();
          labelMap.put(label, label);
        }
        break;
      case 11: 
          TableSwitchInsnNode tinsn = (TableSwitchInsnNode)insn;
          labelMap.put(tinsn.dflt, tinsn.dflt);
          localIterator2 = tinsn.labels.iterator();
          while (localIterator2.hasNext()) {
              LabelNode label2 = (LabelNode)localIterator2.next();
              labelMap.put(label2, label2);
          }
          continue;
      case 14: 
        FrameNode fnode = (FrameNode)insn;
        if (fnode.local != null) {
          for (Object o : fnode.local) {
            if ((o instanceof LabelNode)) {
              labelMap.put((LabelNode)o, (LabelNode)o);
            }
          }
        }
        if (fnode.stack != null) {
          for (Object o : fnode.stack) {
            if ((o instanceof LabelNode)) {
              labelMap.put((LabelNode)o, (LabelNode)o);
            }
          }
        }
        break;
      }
    }
    return labelMap;
  }
  
  public Map<LabelNode, LabelNode> cloneLabels()
  {
    Map<LabelNode, LabelNode> labelMap = identityLabelMap();
    for (Map.Entry<LabelNode, LabelNode> entry : labelMap.entrySet()) {
      entry.setValue(new LabelNode());
    }
    return labelMap;
  }
  
  public InsnListSection copy()
  {
    return copy(cloneLabels());
  }
  
  public InsnListSection copy(Map<LabelNode, LabelNode> labelMap)
  {
    InsnListSection copy = new InsnListSection();
    for (AbstractInsnNode insn : this) {
      copy.add(insn.clone(labelMap));
    }
    return copy;
  }
  
  public Iterator<AbstractInsnNode> iterator()
  {
    return new InsnListSectionIterator();
  }
}


