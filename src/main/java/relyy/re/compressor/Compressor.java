package relyy.re.compressor;

import java.io.IOException;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */
public interface Compressor {

	byte[] compressor(byte[] array)throws IOException;

	byte[] unCompressor(byte[] array)throws IOException;
}
