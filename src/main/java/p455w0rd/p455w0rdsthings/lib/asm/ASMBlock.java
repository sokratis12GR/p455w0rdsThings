package p455w0rd.p455w0rdsthings.lib.asm;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class ASMBlock
{
  public InsnListSection list;
  private BiMap<String, LabelNode> labels;
  
  public ASMBlock(InsnListSection list, BiMap<String, LabelNode> labels)
  {
    this.list = list;
    this.labels = labels;
  }
  
  public ASMBlock(InsnListSection list)
  {
    this(list, HashBiMap.create());
  }
  
  public ASMBlock(InsnList list)
  {
    this(new InsnListSection(list));
  }
  
  public ASMBlock()
  {
    this(new InsnListSection());
  }
  
  public LabelNode getOrAdd(String s)
  {
    LabelNode l = get(s);
    if (l == null) {
      this.labels.put(s, l = new LabelNode());
    }
    return l;
  }
  
  public LabelNode get(String s)
  {
    return (LabelNode)this.labels.get(s);
  }
  
  public void replaceLabels(Map<LabelNode, LabelNode> labelMap, Set<LabelNode> usedLabels)
  {
    for (AbstractInsnNode insn : this.list) {
      switch (insn.getType())
      {
      case 8: 
        AbstractInsnNode insn2 = insn.clone(labelMap);
        if (insn2 != insn)
        {
          if (usedLabels.contains(insn2)) {
            throw new IllegalStateException("LabelNode cannot be a part of two InsnLists");
          }
          this.list.replace(insn, insn2);
        }
        break;
      case 7: 
      case 11: 
      case 12: 
      case 14: 
        this.list.replace(insn, insn.clone(labelMap));
      }
    }
    for (Map.Entry<LabelNode, LabelNode> entry : labelMap.entrySet())
    {
      String key = (String)this.labels.inverse().get(entry.getKey());
      if (key != null) {
        this.labels.put(key, entry.getValue());
      }
    }
  }
  
  public void replaceLabels(Map<LabelNode, LabelNode> labelMap)
  {
    replaceLabels(labelMap, Collections.emptySet());
  }
  
  public void replaceLabel(String s, LabelNode l)
  {
    LabelNode old = get(s);
    if (old != null) {
      replaceLabels(ImmutableMap.of(old, l));
    }
  }
  
  @SuppressWarnings("unchecked")
public ASMBlock mergeLabels(ASMBlock other)
  {
    if ((this.labels.isEmpty()) || (other.labels.isEmpty())) {
      return this;
    }
    HashMap<LabelNode, LabelNode> labelMap = this.list.identityLabelMap();
    for (Map.Entry<String, LabelNode> entry : other.labels.entrySet())
    {
      LabelNode old = (LabelNode)this.labels.get(entry.getKey());
      if (old != null) {
        labelMap.put(old, entry.getValue());
      }
    }
    Object usedLabels = new HashSet<Object>();
    for (AbstractInsnNode insn = other.list.list.getFirst(); insn != null; insn = insn.getNext()) {
      if (insn.getType() == 8) {
        ((HashSet<LabelNode>)usedLabels).add((LabelNode)insn);
      }
    }
    replaceLabels(labelMap, (Set<LabelNode>)usedLabels);
    return this;
  }
  
  public ASMBlock pullLabels(ASMBlock other)
  {
    other.list.remove();
    return mergeLabels(other);
  }
  
  public ASMBlock copy()
  {
    BiMap<String, LabelNode> labels = HashBiMap.create();
    Map<LabelNode, LabelNode> labelMap = this.list.cloneLabels();
    for (Map.Entry<String, LabelNode> entry : this.labels.entrySet()) {
      labels.put(entry.getKey(), labelMap.get(entry.getValue()));
    }
    return new ASMBlock(this.list.copy(labelMap), labels);
  }
  
  public ASMBlock applyLabels(InsnListSection list2)
  {
    if (this.labels.isEmpty()) {
      return new ASMBlock(list2);
    }
    Set<LabelNode> cFlowLabels1 = this.labels.values();
    Set<LabelNode> cFlowLabels2 = InsnComparator.getControlFlowLabels(list2);
    ASMBlock block = new ASMBlock(list2);
    
    HashMap<LabelNode, LabelNode> labelMap = new HashMap<LabelNode, LabelNode>();
    
    int i = 0;
    for (int k = 0; (i < this.list.size()) && (k < list2.size());)
    {
      AbstractInsnNode insn1 = this.list.get(i);
      if (!InsnComparator.insnImportant(insn1, cFlowLabels1))
      {
        i++;
      }
      else
      {
        AbstractInsnNode insn2 = list2.get(k);
        if (!InsnComparator.insnImportant(insn2, cFlowLabels2))
        {
          k++;
        }
        else
        {
          if (insn1.getOpcode() != insn2.getOpcode()) {
            throw new IllegalArgumentException("Lists do not match:\n" + this.list + "\n\n" + list2);
          }
          switch (insn1.getType())
          {
          case 8: 
            labelMap.put((LabelNode)insn1, (LabelNode)insn2);
            break;
          case 7: 
            labelMap.put(((JumpInsnNode)insn1).label, ((JumpInsnNode)insn2).label);
          }
          i++;
          k++;
        }
      }
    }
    for (Map.Entry<String, LabelNode> entry : this.labels.entrySet()) {
      block.labels.put(entry.getKey(), labelMap.get(entry.getValue()));
    }
    return block;
  }
  
  public InsnList rawListCopy()
  {
    return this.list.copy().list;
  }
}


