package com.shenmei.data.sse.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class OrderIdGenerateByPbu {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    // Redis键前缀（区分业务）
    private static final String ORDER_SEQ_PREFIX = "order:seq:";
    // 序列号位数（固定7位）
    private static final int SEQ_LENGTH = 7;

    /**
     * 生成订单号
     * @param pbu 订单所属的PBU（如"12745"）
     * @return 完整订单号（格式：{pbu}{7位序列号}）
     */
    public String generateOrderNo(String pbu) {
        // 1. 构建当前PBU的序列键
        String seqKey = ORDER_SEQ_PREFIX + pbu;

        // 2. 原子自增获取序列号（Redis的INCR命令保证线程安全和分布式唯一性）
        // 若键不存在，会自动初始化为0，然后+1返回1
        Long seq = stringRedisTemplate.opsForValue().increment(seqKey);
        if (seq == null) {
            throw new RuntimeException("生成订单序列号失败，Redis操作异常");
        }

        // 3. 序列号补零（确保7位，如1 → 0000001）
        String formattedSeq = String.format("%0" + SEQ_LENGTH + "d", seq);

        // 4. 组合PBU和序列号生成订单号
        return NumberToLettersConverter.convert(pbu) + formattedSeq;
    }

    /**
     * 可选：设置序列过期时间（防止Redis键无限累积，根据业务需求决定）
     * @param pbu PBU
     * @param expireDays 过期天数
     */
    public void setSeqExpire(String pbu, int expireDays) {
        String seqKey = ORDER_SEQ_PREFIX + pbu;
        stringRedisTemplate.expire(seqKey, expireDays, TimeUnit.DAYS);
    }

    /**
     * 可选：手动重置某个PBU的序列（谨慎使用，可能导致重复）
     * @param pbu PBU
     * @param resetValue 重置值（如1）
     */
    public void resetSeq(String pbu, long resetValue) {
        String seqKey = ORDER_SEQ_PREFIX + pbu;
        stringRedisTemplate.opsForValue().set(seqKey, String.valueOf(resetValue));
    }
}
