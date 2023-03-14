import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** @github <a href="https://github.com/felondrum/yandex_algo_sprint_one_final">...</a> */
public class Main {
  public static void main(String[] args) {
    makeTests(); //comment row to skip tests
    readFromConsole(0); //switch to 2 for O(N) algo
  }

  /** @param algoNum - номер алгоритма, 0 - выход их метода, 1 - N^2, 2 - N
  * */
  public static void readFromConsole(int algoNum) {
    if (algoNum==0) return;
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      if (algoNum==1) {
        writeArray(getClosersToZeroElementNaive(Integer.parseInt(reader.readLine()), readArray(reader)));
      } else {
        writeArray(getClosersToZeroElementObyN(Integer.parseInt(reader.readLine()), readArray(reader)));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Naive: O(N^2) Failed by time limit on 1_000_000-size array (YContext: ID 83892912)
   @param n - start array size, 0 < n < 10^6
   @param startArr - array of int startArr.size()<=10^9
   @return array of int with element position to closer zero*/
  //
  public static List<Integer> getClosersToZeroElementNaive (int n, List<Integer> startArr) {
    List<Integer> zeroArr = new ArrayList<>();
    //O(N)
    for (int i=0; i<n; i++) {
      if (startArr.get(i)==0) zeroArr.add(i);
    }
    //O(N^2)
    for (int j=0; j<n; j++) {
      if (startArr.get(j)==0) {
        startArr.set(j, 0);
      } else {
        int minVal = n;
        for (int z : zeroArr) {
          if (minVal > Math.abs(j - z)) {
            minVal = Math.abs(j - z);
          }
        }
        startArr.set(j, minVal);
      }
    }
    return startArr;
  }


  /** O(N) Success on YContext (YContext: ID 83892764)
   @param n - start array size, 0 < n < 10^6
   @param startArr - array of int startArr.size()<=10^9
   @return array of int with element position to closer zero*/
  //
  public static List<Integer> getClosersToZeroElementObyN (int n, List<Integer> startArr) {
    boolean hasZero = false;
    boolean onlyZeros = false;
    int lastZeroPosition = -1;
    //O(N)
    for (int i=0; i<n; i++) {
      if (startArr.get(i)==0) {
        startArr.set(i, 0);
        if (lastZeroPosition == i-1) onlyZeros = true;
        if (!onlyZeros) {
          int distanceFromZeroCorrection = 1;
          int backCorrectionDeep = hasZero ? getCenterPosition(lastZeroPosition, i) : -1;
          for (int j=i-1; j > backCorrectionDeep; j--) {
            startArr.set(j, distanceFromZeroCorrection);
            distanceFromZeroCorrection++;
          }
        }
        lastZeroPosition=i;
        hasZero = true;
      } else {
        if (!hasZero) {
          startArr.set(i, -1);
        } else {
          startArr.set(i, i-lastZeroPosition);
        }
        onlyZeros = false;
      }
    }
    return startArr;
  }

  public static Integer getCenterPosition(int start, int end) {
    return end - ((end-start)/2 + (end-start)%2);
  }

  public static List<Integer> readArray(BufferedReader reader) throws IOException {
  return Arrays.asList(reader.readLine().strip().split(" "))
          .stream()
          .map(Integer::parseInt)
          .collect(Collectors.toList());
  }

  public static void writeArray(List<? extends Number> arr) throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
    if (arr.size() == 0) {
      writer.write("None");
    } else {
      for (Number i : arr) {
        writer.write(String.valueOf(i));
        writer.write(" ");
      }
    }
    writer.flush();
  }

  public static void makeTests() {
    List<List<Integer>> testList = new ArrayList<>();
    List<List<Integer>> resultList = new ArrayList<>();
    testList.add(Arrays.asList(0,0,0,0,0)); resultList.add(Arrays.asList(0,0,0,0,0)); //1
    testList.add(Arrays.asList(0,1,4,9,0)); resultList.add(Arrays.asList(0,1,2,1,0)); //2 from yandex context
    testList.add(Arrays.asList(0,7,9,4,8,20)); resultList.add(Arrays.asList(0,1,2,3,4,5)); //3 from yandex context
    testList.add(Arrays.asList(0,0,0,0,0,20)); resultList.add(Arrays.asList(0,0,0,0,0,1)); //4
    testList.add(Arrays.asList(3,0,0,0,0)); resultList.add(Arrays.asList(1,0,0,0,0)); //5
    testList.add(Arrays.asList(2,0,1,0,5)); resultList.add(Arrays.asList(1,0,1,0,1)); //6
    testList.add(Arrays.asList(0,3,0,2,0)); resultList.add(Arrays.asList(0,1,0,1,0)); //7
    testList.add(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,0)); resultList.add(Arrays.asList(16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0)); //8
    testList.add(Arrays.asList(3,0,1,0,5,6,7,8,9,0,10,12,11,2,0,0,0)); resultList.add(Arrays.asList(1,0,1,0,1,2,3,2,1,0,1,2,2,1,0,0,0)); //9
    for (int i = 0; i<resultList.size(); i++) {
      exTest(testList.get(i), resultList.get(i), i+1);
    }
  }

  public static void exTest(List<Integer> testList, List<Integer> correctResult, int testCount) {
    getTestResult(getClosersToZeroElementNaive(testList.size(), testList), correctResult, testCount, "N^2");
    getTestResult(getClosersToZeroElementObyN(testList.size(), testList), correctResult, testCount, "N");
  }
  public static void getTestResult(List<Integer> testedList, List<Integer> correctResult, int testCount, String algoName) {
    if (testedList.size() != correctResult.size()) {
      System.out.println("Error in test:" + testCount + ". Answer size! algo: " + algoName);
      return;
    }
    for (int i = 0; i < correctResult.size(); i++) {
      if (!testedList.get(i).equals(correctResult.get(i))) {
        System.out.println("Error in test: " + testCount
                + ", result in pos " + i
                + ", has: " + testedList + " expected: " + correctResult
                + ", algo: " + algoName);
        return;
      }
    }
    System.out.println("Test #" + testCount + " correct!"
            + ", has: " + testedList + " expected: " + correctResult
            + ", algo: " + algoName);
  }

}



