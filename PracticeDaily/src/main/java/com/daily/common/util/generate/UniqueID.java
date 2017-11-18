package com.daily.common.util.generate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author wencheng
 * @Description:
 * @date 2017/11/6
 */
public class UniqueID {
    private static Logger logger = LoggerFactory.getLogger(UniqueID.class);
    private static IdWorker idWorder;

    public UniqueID() {
    }

    public static synchronized long generateId() {
        return idWorder.nextId();
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    static {
        int machineNo = Integer.parseInt(LocalHostUtil.getLastSegment());
        idWorder = new IdWorker((long) machineNo);
        logger.info("当前运行机器LastSegmentIP:" + machineNo);
    }
}
