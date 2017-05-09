/*
 * Copyright 2016 leon chen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moilioncircle.redis.replicator.cmd.parser;

import com.moilioncircle.redis.replicator.cmd.CommandParser;
import com.moilioncircle.redis.replicator.cmd.impl.AggregateType;
import com.moilioncircle.redis.replicator.cmd.impl.ZInterStoreCommand;

import java.math.BigDecimal;

/**
 * @author Leon Chen
 * @since 2.1.0
 */
public class ZInterStoreParser implements CommandParser<ZInterStoreCommand> {
    @Override
    public ZInterStoreCommand parse(Object[] command) {
        int idx = 1;
        AggregateType aggregateType = null;
        String destination = (String) command[idx++];
        int numkeys = new BigDecimal((String) command[idx++]).intValueExact();
        String[] keys = new String[numkeys];
        for (int i = 0; i < numkeys; i++) {
            keys[i] = (String) command[idx++];
        }
        double[] weights = null;
        while (idx < command.length) {
            String param = (String) command[idx];
            if ("WEIGHTS".equalsIgnoreCase(param)) {
                idx++;
                weights = new double[numkeys];
                for (int i = 0; i < numkeys; i++) {
                    weights[i] = Double.parseDouble((String) command[idx++]);
                }
            }
            if ("AGGREGATE".equalsIgnoreCase(param)) {
                idx++;
                String next = (String) command[idx++];
                if ("SUM".equalsIgnoreCase(next)) {
                    aggregateType = AggregateType.SUM;
                } else if ("MIN".equalsIgnoreCase(next)) {
                    aggregateType = AggregateType.MIN;
                } else if ("MAX".equalsIgnoreCase(next)) {
                    aggregateType = AggregateType.MAX;
                }
            }
        }
        return new ZInterStoreCommand(destination, numkeys, keys, weights, aggregateType);
    }

}
