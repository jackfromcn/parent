package com.daily.common.util;

import java.util.ArrayList;
import java.util.List;


public class TaskRecordUtil<T> {
    
    /**
     * 将recordStart<=  X  <recordEnd 间的数据
     * 按照batchSize进行分批归类
     * @param recordStart  起始数  0开始
     * @param recordEnd
     * @param batchSize
     */
    public List<TaskRecord<T>> makeTaskRecordListByBatchSize(Integer recordStart,Integer recordEnd,Integer batchSize){
        List<TaskRecord<T>> list = new ArrayList<TaskRecord<T>>();
        TaskRecord<T> taskRecord = null;
        int count = recordEnd-recordStart;
        if (count <= batchSize) {
            taskRecord = new TaskRecord<T>(recordStart, recordEnd, count, 1);
            list.add(taskRecord);
            return list;
        }
        
        for(int i= recordStart;i<recordEnd;i=i+batchSize){
            Integer dataSize=batchSize;
            if(recordEnd<(i+batchSize)){
                dataSize=recordEnd-i;
            }
            taskRecord = new TaskRecord<T>(i, (i+dataSize), dataSize, i);
            list.add(taskRecord);
        }
        return list;
    }
    
     public static void main(String[] args) {
		TaskRecordUtil taskRecordUtil=new TaskRecordUtil<Long>();
		taskRecordUtil.makeTaskRecordListByBatchSize(0, 999, 100);
	}
    
    
    /**
     * 将分页获取数据，按照batchSize分批处理。
     * 每批数据按照 recordStart<=  X  <recordEnd 进行归类
     * @param allData  分页取出的数据
     * @param batchSize  每批数据的大小
     * @return
     */
    public List<TaskRecord<T>> makeTaskRecordListByBatchSize(List<T> allData, Integer batchSize) {
        List<TaskRecord<T>> list = new ArrayList<TaskRecord<T>>();
        TaskRecord<T> taskRecord = null;
        int count = allData.size();
        if (count <= batchSize) {
            taskRecord = new TaskRecord<T>(0, count, allData, 1);
            list.add(taskRecord);
            return list;
        }
        int batchNum = calculateThreadNum(count, batchSize);
        for (int i = 1; i <= batchNum; i++) {
            int recordStart = (i - 1) * batchSize;
            int recordEnd = batchSize * i;
            if (recordEnd > count) {
                recordEnd = count;
            }
            List<T> data = allData.subList(recordStart, recordEnd);
            taskRecord = new TaskRecord<T>(recordStart, recordEnd, data, i);
            list.add(taskRecord);
            if(recordEnd == count){
                break;
            }
        }
        return list;
    }
    
    
    /**
     * 计算每个线程平均存放的任务数
     * @param dataCount 总记录数
     * @param threadNum 启动的线程数量
     * @return 每个线程中需要存放的任务数量，除不尽的任务，放入最后一个线程执行
     */
    private  Integer calculateThreadSize(int dataCount, int threadNum) {
        return  dataCount/threadNum;
    }
    
    private  Integer calculateThreadNum(int dataCount, int threadSize) {
        int totalPageNums = dataCount/threadSize;
        return dataCount%threadSize ==0 ? totalPageNums:totalPageNums + 1;
    }
    
}
