package p455w0rd.p455w0rdsthings.lib.render;

import java.util.ArrayList;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CCRenderPipeline
{
  public class PipelineBuilder
  {
    public PipelineBuilder() {}
    
    public PipelineBuilder add(CCRenderState.IVertexOperation op)
    {
      CCRenderPipeline.this.ops.add(op);
      return this;
    }
    
    public PipelineBuilder add(CCRenderState.IVertexOperation... ops)
    {
      for (int i = 0; i < ops.length; i++) {
        CCRenderPipeline.this.ops.add(ops[i]);
      }
      return this;
    }
    
    public void build()
    {
      CCRenderPipeline.this.rebuild();
    }
    
    public void render()
    {
      CCRenderPipeline.this.rebuild();
      CCRenderState.render();
    }
  }
  
  private class PipelineNode
  {
    public ArrayList<PipelineNode> deps = new ArrayList();
    public CCRenderState.IVertexOperation op;
    
    private PipelineNode() {}
    
    public void add()
    {
      if (this.op == null) {
        return;
      }
      for (int i = 0; i < this.deps.size(); i++) {
        ((PipelineNode)this.deps.get(i)).add();
      }
      this.deps.clear();
      CCRenderPipeline.this.sorted.add(this.op);
      this.op = null;
    }
  }
  
  private ArrayList<CCRenderState.VertexAttribute> attribs = new ArrayList();
  private ArrayList<CCRenderState.IVertexOperation> ops = new ArrayList();
  private ArrayList<PipelineNode> nodes = new ArrayList();
  private ArrayList<CCRenderState.IVertexOperation> sorted = new ArrayList();
  private PipelineNode loading;
  private PipelineBuilder builder = new PipelineBuilder();
  
  public void setPipeline(CCRenderState.IVertexOperation... ops)
  {
    this.ops.clear();
    for (int i = 0; i < ops.length; i++) {
      this.ops.add(ops[i]);
    }
    rebuild();
  }
  
  public void reset()
  {
    this.ops.clear();
    unbuild();
  }
  
  
private void unbuild()
  {
    for (int i = 0; i < this.attribs.size(); i++) {
      ((CCRenderState.VertexAttribute)this.attribs.get(i)).active = false;
    }
    this.attribs.clear();
    this.sorted.clear();
  }
  
  public void rebuild()
  {
    if ((this.ops.isEmpty()) || (CCRenderState.model == null)) {
      return;
    }
    while (this.nodes.size() < CCRenderState.operationCount()) {
      this.nodes.add(new PipelineNode());
    }
    unbuild();
    if (CCRenderState.fmt.hasNormal()) {
      addAttribute(CCRenderState.normalAttrib);
    }
    if (CCRenderState.fmt.hasColor()) {
      addAttribute(CCRenderState.colourAttrib);
    }
    if (CCRenderState.computeLighting) {
      addAttribute(CCRenderState.lightingAttrib);
    }
    for (int i = 0; i < this.ops.size(); i++)
    {
      CCRenderState.IVertexOperation op = (CCRenderState.IVertexOperation)this.ops.get(i);
      this.loading = ((PipelineNode)this.nodes.get(op.operationID()));
      boolean loaded = op.load();
      if (loaded) {
        this.loading.op = op;
      }
      if ((op instanceof CCRenderState.VertexAttribute)) {
        if (loaded) {
          this.attribs.add((CCRenderState.VertexAttribute)op);
        } else {
          ((CCRenderState.VertexAttribute)op).active = false;
        }
      }
    }
    for (int i = 0; i < this.nodes.size(); i++) {
      ((PipelineNode)this.nodes.get(i)).add();
    }
  }
  
  public void addRequirement(int opRef)
  {
    this.loading.deps.add(this.nodes.get(opRef));
  }
  
  public void addDependency(CCRenderState.VertexAttribute attrib)
  {
    this.loading.deps.add(this.nodes.get(attrib.operationID()));
    addAttribute(attrib);
  }
  
  public void addAttribute(CCRenderState.VertexAttribute attrib)
  {
    if (!attrib.active)
    {
      this.ops.add(attrib);
      attrib.active = true;
    }
  }
  
  public void operate()
  {
    for (int i = 0; i < this.sorted.size(); i++) {
      ((CCRenderState.IVertexOperation)this.sorted.get(i)).operate();
    }
  }
  
  public PipelineBuilder builder()
  {
    this.ops.clear();
    return this.builder;
  }
}


