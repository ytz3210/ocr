package com.el.util;

import java.io.*;


 /**
  * File工具类
  * @author Yangtz
  * @date 2019/8/9
  */
public class FileUtils {

    /**
     * IO流复制
     * @param out
     * @param in
     * @return
     * @throws Exception
     */
    public static void copy(InputStream in, OutputStream out) {
        InputStream _in = null;
        OutputStream _out = null;
        try {
            _in = new BufferedInputStream(in);
            _out = new BufferedOutputStream(out);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = _in.read(b, 0, b.length)) != -1) {
                _out.write(b, 0, len);
            }
            _out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(_in, _out);
        }
    }
 /**
  * 关闭流
  * @author Yangtz
  * @date 2019/8/9
  * @param in
  * @param out
  */
    public static void close(InputStream in, OutputStream out) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                in = null;
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                in = null;
            }
        }
    }
}
