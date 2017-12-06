package com.daily.common.util;

import java.util.List;


public class TaskRecord<T> {
    /** 任务线程开始记录 */
    private int recordStart;
    /** 任务线程结束记录 */
    private int recordEnd;
    /** 当前线程包含的数据数 **/
    private int dataSize;
    /** 线程数 */
    private int threadSize;
    
    private List<T> data;
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + dataSize;
        result = prime * result + recordEnd;
        result = prime * result + recordStart;
        result = prime * result + threadSize;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskRecord<T> other = (TaskRecord<T>) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (dataSize != other.dataSize)
            return false;
        if (recordEnd != other.recordEnd)
            return false;
        if (recordStart != other.recordStart)
            return false;
        if (threadSize != other.threadSize)
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "current:"+threadSize+"   data_size:"+dataSize+"   data_scope :"+recordStart+"<= record <"+recordEnd;
    }
    
    public String toBatchString() {
        // TODO Auto-generated method stub
        return "current:"+threadSize+"   data_size:"+data.size()+"   data_scope :"+recordStart+"<= record <"+recordEnd;
    }
    
    public int getRecordStart() {
        return recordStart;
    }
    
    public void setRecordStart(int recordStart) {
        this.recordStart = recordStart;
    }
    
    public int getRecordEnd() {
        return recordEnd;
    }
    
    public void setRecordEnd(int recordEnd) {
        this.recordEnd = recordEnd;
    }
    
    public int getDataSize() {
        return dataSize;
    }
    
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
    
    public int getThreadSize() {
        return threadSize;
    }
    
    public void setThreadSize(int threadSize) {
        this.threadSize = threadSize;
    }
    
    public List<T> getData() {
        return data;
    }
    
    public void setData(List<T> data) {
        this.data = data;
    }
    public TaskRecord(int recordStart, int recordEnd, int dataSize, int threadSize){
        super();
        this.recordStart = recordStart;
        this.recordEnd = recordEnd;
        this.dataSize = dataSize;
        this.threadSize = threadSize;
    }
    public TaskRecord(int recordStart, int recordEnd, List<T> data, int threadSize){
        super();
        this.recordStart = recordStart;
        this.recordEnd = recordEnd;
        this.data = data;
        this.threadSize = threadSize;
    }
    
}
