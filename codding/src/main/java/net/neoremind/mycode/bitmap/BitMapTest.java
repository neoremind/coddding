package net.neoremind.mycode.bitmap;

public class BitMapTest {
	
    private final int[] bitmap;
    private final int size;
    public BitMapTest(final int size) {
        this.size = size;
        int sLen = ((size%32) == 0) ? size/32 : size/32 + 1;
        this.bitmap = new int[sLen];
    }
       
    private static int _Index(final int number ){
        return number / 32;
    }
       
    private static int _Position(final int number){
        return number % 32;
    }
       
    private void adjustBitMap(final int index, final int position) {
        int bit = bitmap[index] | (1 << position);
        bitmap[index] = bit;
    }
       
    public void add(int[] numArr){
        for(int i=0; i<numArr.length; i++)
            add(numArr[i]);
    }
       
    public void add(int number) {
        adjustBitMap(_Index(number),_Position(number));
    }
       
    public boolean getIndex(final int index) {
        if(index > size) return false;
           
        int bit = (bitmap[_Index(index)] >> _Position(index)) & 0x0001;
        return (bit == 1);
    }
   
    @Override
    public String toString() {
        StringBuffer sbf = new StringBuffer(size);
        for(Integer i : bitmap) {
            StringBuffer tmp = new StringBuffer(32);
            String bits = Integer.toBinaryString(i);
            for(int b=0; b<32-bits.length(); b++)
                tmp.append(0);
            tmp.append(bits);
            sbf.append(tmp.reverse());
        }
        String s = sbf.substring(0, size).toString();
//      System.out.println("bitmap length: " + bitmap.length + " \r\nsize:" + size);
        return new StringBuffer(s).reverse().toString();
    }
       
    public static void main(String[] args) {
    	System.out.println(Integer.MAX_VALUE);
    	BitMapTest bm = new BitMapTest(1024);
//      int searchNum = 56;
        int searchNum = 99;
        int[] numArr = {19, 64, 45, 56, 0, 54, 28, 2, 23, 34, 40, 18, 54, 50, 49, 29, 20, 31, 47, 30, 24, 17, 50, 57, 33, 55, 21, 22, 27, 45, 3, 19, 17, 49, 24, 5, 15, 24, 27, 35, 6, 53, 9, 61, 4, 6, 12, 23, 52, 48, 39, 39, 21, 1, 11};
        bm.add(numArr);
        for(int i : numArr) {
            System.out.println(bm.getIndex(i));
        }
        
        System.out.println(bm);
        
        System.out.println(bm.getIndex(searchNum));
    }
       
}
