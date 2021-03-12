package relyy.re.serialization;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */
public class SerializationFactory {

	public static Serialization get(byte type){
		switch (type & 0x7){
			case 0x0:
				return new HessianSerialization();
			default:
				return new HessianSerialization();
		}
	}
}
