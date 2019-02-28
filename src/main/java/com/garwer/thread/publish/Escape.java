package com.garwer.thread.publish;

/**
 * @Author: Garwer
 * @Date: 19/2/22 上午12:02
 * @Version 1.0
 * 在对象未完成构造前不可以直接发布
 */
public class Escape {
    private int escape = 0;

    public Escape() {

    }

    private class InnerClass {
        public InnerClass() {
            //this 引用逸出 新线程会提早看到它
            System.out.println(Escape.this.escape);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
