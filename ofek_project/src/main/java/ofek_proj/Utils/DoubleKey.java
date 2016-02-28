package ofek_proj.Utils;

public class DoubleKey<T> {
	private T FirstKey;
	private T SecondKey;
	public DoubleKey(T FirstKey,T SecondKey){
		this.FirstKey=FirstKey;
		this.SecondKey=SecondKey;
	}
	public T GetFirstKey(){
		return FirstKey;
	}
	public T GetSecondKey(){
		return SecondKey;
	}
}
