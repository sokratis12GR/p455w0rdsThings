package p455w0rd.p455w0rdsthings.util;

public class TimeUtils
{
	public static TimeUtils INSTANCE;
  protected float timer = 0.0F;
  
  public class Timer
  {
    public Timer(TimeUtils x) {
    	INSTANCE = new TimeUtils();
    }
    
    public float getTimer()
    {
      return TimeUtils.this.timer;
    }
    
    public void setTimer(float time)
    {
      TimeUtils.this.timer = time;
    }
  }
}


