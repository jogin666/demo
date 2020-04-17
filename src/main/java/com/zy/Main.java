package com.zy;

import java.io.*;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        //求在线人数
        firstCount();   //借助 HashSet 实现
//        secondCount();   //采用 布隆过滤器 的思路（存在误差）

        //求最小连接数
//        minPath("A","K");
         minPath("STACEY_STRIMPLE","RICH_OMLI");
//        minPath("MYLES_JEFFCOAT","LANNY_TIBURCIO");
//        minPath("SHAD_BUSSARD","RON_SCIONEAUX");
//        minPath("MYLES_JEFFCOAT","COY_NIKACH");
    }


    public static void firstCount() {
        long start = System.currentTimeMillis();
        HashSet<String> nameSet = new HashSet<>();
        try(
            InputStream is = Main.class.getClassLoader().getResourceAsStream("SocialNetwork.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ){
            String line;
            while ((line=reader.readLine())!=null){
                String[] names = line.split(",");
                nameSet.addAll(Arrays.asList(names));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        long countTime = System.currentTimeMillis() - start;
        System.out.printf("总数->[ %s ] 个,耗时->[ %s ] ms\n", nameSet.size(), countTime);
    }

    /**
     *
     */
    public static void secondCount() {
        long start = System.currentTimeMillis();
        int count = 0;
        try(
            InputStream is = Main.class.getClassLoader().getResourceAsStream("SocialNetwork.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String line;
            List<String> nameList=new ArrayList<>();
            while ((line=reader.readLine())!=null){
                String[] names = line.split(",");
                nameList.addAll(Arrays.asList(names));
            }
            int len = nameList.size();
            boolean[] exit = new boolean[len];
            for (String name : nameList) {
                int hashCode = name.hashCode();
                int position1=Math.abs(hashCode%len);
                int position2=Math.abs((hashCode ^ hashCode>>>2)%len);
                int position3=Math.abs((hashCode ^ hashCode>>>4)%len);
                int position4=Math.abs((hashCode ^ hashCode>>>8)%len);
                int position5=Math.abs((hashCode ^ hashCode>>>16)%len);
                boolean t = exit[position1] && exit[position2] && exit[position3] && exit[position4] && exit[position5];
                if (!t) {
                    exit[position1] = exit[position2] = exit[position3] = exit[position4] = exit[position5]=true;
                    count++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        long countTime = System.currentTimeMillis() - start;
        System.out.printf("总数->[ %s ] 个,耗时->[ %s ] ms\n", count, countTime);
    }


    public static void minPath(String sName,String eName){
        long start = System.currentTimeMillis();
        HashMap<String,List<String>> friendMap=recordFriend(); //存储好友，

        Queue<ArrayList<String>> queue=new LinkedList<>();
        queue.add(new ArrayList<>());
        queue.peek().add(sName);
        ArrayList<String> pathList = new ArrayList<>();

        boolean t=false;
        while (!queue.isEmpty() && !t){
            pathList=queue.poll();
            String name = pathList.get(pathList.size() - 1);
            if (eName.equals(name)){
                t=true;
                break;
            }
            List<String> friendList = friendMap.get(name);
            for (int i=0;friendList!=null && i<friendList.size();i++){
                /**
                 * 接上之前探索的
                 */
                String friendName = friendList.get(i);
                if (!pathList.contains(friendName)){
                    ArrayList<String> list=new ArrayList<>(pathList);
                    list.add(friendName);
                    queue.add(list);
                }
            }
            friendMap.remove(name);
        }
        int minCount=0;
        if (t){
            int size=pathList.size();
            /**
             * 两人认识距离是1
             * 隔一人距离是2
             */
            System.out.println(pathList);
            minCount=(size==2 ? 1 : size-1);
        }
        long countTime = System.currentTimeMillis() - start;
        System.out.printf("最小连接数->[ %s ] 个,耗时->[ %s ] ms\n", minCount, countTime);
    }

    public static HashMap<String,List<String>> recordFriend() {
        HashMap<String, List<String>> friendMap = new HashMap<>();
        try (
            InputStream is = Main.class.getClassLoader().getResourceAsStream("SocialNetwork.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                /**
                 * 好友是相互的
                 */
                String[] names = line.split(",");
                if(friendMap.containsKey(names[0])){
                    friendMap.get(names[0]).add(names[1]);
                }else {
                    ArrayList<String> friendsList = new ArrayList<>();
                    friendsList.add(names[1]);
                    friendMap.put(names[0],friendsList);
                }

                if(friendMap.containsKey(names[1])){
                    friendMap.get(names[1]).add(names[0]);
                }else {
                    ArrayList<String> friendsList = new ArrayList<>();
                    friendsList.add(names[0]);
                    friendMap.put(names[1],friendsList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendMap;
    }
}