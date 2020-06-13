package com.chanshiyu.yuko.disruptor.wapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SHIYU
 * @description 传输的数据
 * @since 2020-06-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorDataWrapper {

    private int command;

    private Object target;

}
