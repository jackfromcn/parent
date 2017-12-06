package com.daily.common.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 创建和解析csv文件，所有的csv文件都必须定义头，csv文件格式兼容excel
 * Created by wangwenyao on 2016-08-09.
 */
public class CsvUtils {

    private static CSVFormat FORMAT = CSVFormat.EXCEL.withQuote(null);

    private static Logger logger = LoggerFactory.getLogger(CsvUtils.class);

    /**
     * 创建csv文件
     *
     * @param records 写入的内容
     * @param stream  写入流
     * @param <T>
     */
    public static <T extends Object> void create(List<List<T>> records, OutputStream stream) {
        create(StandardCharsets.UTF_8, null, records, stream);
    }

    /**
     * 创建csv文件
     *
     * @param headers 文件头字段
     * @param records 写入的内容
     * @param stream  写入流
     * @param <T>
     */
    public static <T extends Object> void create(String[] headers, List<List<T>> records, OutputStream stream) {
        create(StandardCharsets.UTF_8, headers, records, stream);
    }

    /**
     * 创建csv文件
     *
     * @param charset 文件字符集
     * @param headers 文件头字段
     * @param records 写入的内容
     * @param stream  写入流
     * @param <T>
     */
    public static <T extends Object> void create(Charset charset, String[] headers, List<List<T>> records, OutputStream stream) {
        CSVPrinter csvPrinter = null;
        CSVFormat format = headers == null ? FORMAT : FORMAT.withHeader(headers);
        try {
            csvPrinter = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(stream, charset)), format);
            for (List<T> line : records) {
                csvPrinter.printRecord(line);
            }
        } catch (Exception e) {
            logger.error("创建CSV文件，写入内容出现异常", e);
        } finally {
            try {
                if (csvPrinter != null) {
                    csvPrinter.flush();
                    csvPrinter.close();
                }
            } catch (IOException e) {
                logger.error("创建CSV文件，关闭输出流现异常", e);
            }
        }
    }

    /**
     * 解析csv文件，按照文件列顺序返回记录
     *
     * @param file 文件，默认utf-8编码
     * @return
     */
    public static List<List<String>> parse(File file) {
        try {
            return parse(new FileInputStream(file), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 解析csv文件，按照文件列顺序返回记录
     *
     * @param file    文件
     * @param charset 文件字符集
     * @return
     */
    public static List<List<String>> parse(File file, Charset charset) {
        try {
            return parse(new FileInputStream(file), charset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    /**
     * 解析csv文件，按照文件列顺序返回记录
     *
     * @param inputStream 文件输入流
     * @param charset     文件字符集
     * @return
     */
    public static List<List<String>> parse(InputStream inputStream, Charset charset) {
        CSVParser csvParser = null;
        try {
            csvParser = new CSVParser(new BufferedReader(new InputStreamReader(inputStream, charset)), FORMAT);
            List<CSVRecord> csvRecords = csvParser.getRecords();
            CSVRecord csvRecord;
            List<List<String>> records = new ArrayList<>(csvRecords.size());
            String column;
            for (int i = 0; i < csvRecords.size(); i++) {
                csvRecord = csvRecords.get(i);
                List<String> record = new ArrayList<>(csvRecord.size());
                for (int j = 0; j < csvRecord.size(); j++) {
                    column = csvRecord.get(j);
                    if (column != null) {
                        record.add(column.trim());
                    } else {
                        record.add(column);
                    }
                }
                records.add(record);
            }
            return records;
        } catch (Exception e) {
            logger.error("解析CSV文件，解析内容异常", e);
        } finally {
            try {
                if (csvParser != null) {
                    csvParser.close();
                }
            } catch (IOException e) {
                logger.error("解析CSV文件，关闭输入流异常", e);
            }
        }
        return null;
    }

    /**
     * 解析csv文件，按照给定的文件头顺序解析返回记录
     *
     * @param headers     指定需要解析的头
     * @param inputStream 文件流
     * @param charset     文件字符集
     * @return
     */
    public static List<List<String>> parse(String[] headers, InputStream inputStream, Charset charset) {
        CSVParser csvParser = null;
        try {
            CSVFormat format = FORMAT.withHeader(headers);
            csvParser = new CSVParser(new BufferedReader(new InputStreamReader(inputStream, charset)), format);
            List<CSVRecord> csvRecords = csvParser.getRecords();
            CSVRecord csvRecord;
            List<List<String>> records = new ArrayList<>(csvRecords.size());
            List<String> record;
            String column;
            for (int i = 1; i < csvRecords.size(); i++) {
                csvRecord = csvRecords.get(i);
                record = new ArrayList<>(csvRecord.size());
                for (String head : headers) {
                    column = csvRecord.get(head);
                    if (column != null) {
                        record.add(column.trim());
                    } else {
                        record.add(column);
                    }
                }
                records.add(record);
            }
            return records;
        } catch (Exception e) {
            logger.error("解析CSV文件，解析内容异常", e);
        } finally {
            try {
                if (csvParser != null) {
                    csvParser.close();
                }
            } catch (IOException e) {
                logger.error("解析CSV文件，关闭输入流异常", e);
            }
        }
        return null;
    }

    /**
     * 解析csv文件，按照给定的文件头顺序解析返回记录
     *
     * @param headers csv文件命名列，一般第一列
     * @param file    文件
     * @param charset 文件字符集
     * @return
     */
    public static List<List<String>> parse(String[] headers, File file, Charset charset) {
        try {
            return parse(headers, new FileInputStream(file), charset);
        } catch (FileNotFoundException e) {
            logger.error("解析CSV文件，文件不存在异常", e);
        }
        return Collections.emptyList();
    }

    /**
     * 解析csv文件，按照给定的文件头顺序解析返回记录
     *
     * @param headers csv文件命名列，一般第一列
     * @param file    文件，默认utf-8编码
     * @return
     */
    public static List<List<String>> parse(String[] headers, File file) {
        try {
            return parse(headers, new FileInputStream(file), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            logger.error("解析CSV文件，文件不存在异常", e);
        }
        return Collections.emptyList();
    }
}
