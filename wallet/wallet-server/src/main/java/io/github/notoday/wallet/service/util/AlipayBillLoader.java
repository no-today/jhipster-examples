package io.github.notoday.wallet.service.util;

import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/**
 * @author no-today
 * @date 2021/10/20 4:06 PM
 */
public class AlipayBillLoader {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        String filename = "/Users/notoday/Downloads/alipay_record_20211020_1603_1.csv";

        AtomicBoolean effective = new AtomicBoolean(false);
        String csvStr = FileUtils.readLines(FileUtils.getFile(filename), CharsetUtil.GBK).stream()
            .filter(e -> {
                if (e.startsWith("-")) {
                    effective.set(!effective.get());
                    return false;
                }
                return effective.get();
            }).collect(Collectors.joining("\n"));

        CsvReader reader = CsvUtil.getReader(CsvReadConfig.defaultConfig().setBeginLineNo(1));
        reader.readFromStr(csvStr, row -> {
            /*
             * [0]交易号
             * [1]商家订单号
             * [2]交易创建时间
             * [3]付款时间
             * [4]最近修改时间
             * [5]交易来源地
             * [6]类型
             * [7]交易对方
             * [8]商品名称
             * [9]金额（元）
             * [10]收/支
             * [11]交易状态
             * [12]服务费（元）
             * [13]成功退款（元）
             * [14]备注
             * [15]资金状态
             */
            AlipayTransactionRecord record = new AlipayTransactionRecord(
                string(row.get(0)),
                string(row.get(1)),
                date(row.get(2)),
                date(row.get(3)),
                date(row.get(4)),
                string(row.get(5)),
                string(row.get(6)),
                string(row.get(7)),
                string(row.get(8)),
                Double.parseDouble(row.get(9).trim()),
                string(row.get(10)),
                string(row.get(11)),
                Double.parseDouble(row.get(12).trim()),
                Double.parseDouble(row.get(13).trim()),
                string(row.get(14)),
                string(row.get(15))
            );

            System.out.println(record);
        });
    }

    private static String string(String str) {
        return Optional.ofNullable(str).map(e -> e.trim().replaceAll("\t", "")).orElse(null);
    }

    private static LocalDateTime date(String str) {
        return Optional.ofNullable(StringUtils.isNotBlank(str) ? str : null).map(e -> LocalDateTime.parse(e.trim(), DATE_TIME_FORMATTER)).orElse(null);
    }

    @Data
    @NoArgsConstructor
    public static class AlipayTransactionRecord {
        private String orderNo;
        private String merchantOrderNo;
        private LocalDateTime createdDate;
        private LocalDateTime paymentDate;
        private LocalDateTime lastModifiedDate;
        private String source;
        private String tag;
        private String target;
        private String project;
        private Double amount;
        private String inOut;
        private String paymentStatus;
        private Double serviceCharge;
        private Double successfulRefund;
        private String remark;
        private String fundStatus;

        public AlipayTransactionRecord(String orderNo, String merchantOrderNo, LocalDateTime createdDate, LocalDateTime paymentDate, LocalDateTime lastModifiedDate, String source, String tag, String target, String project, Double amount, String inOut, String paymentStatus, Double serviceCharge, Double successfulRefund, String remark, String fundStatus) {
            this.orderNo = orderNo;
            this.merchantOrderNo = merchantOrderNo;
            this.createdDate = createdDate;
            this.paymentDate = paymentDate;
            this.lastModifiedDate = lastModifiedDate;
            this.source = source;
            this.tag = tag;
            this.target = target;
            this.project = project;
            this.amount = amount;
            this.inOut = inOut;
            this.paymentStatus = paymentStatus;
            this.serviceCharge = serviceCharge;
            this.successfulRefund = successfulRefund;
            this.remark = remark;
            this.fundStatus = fundStatus;
        }
    }
}
