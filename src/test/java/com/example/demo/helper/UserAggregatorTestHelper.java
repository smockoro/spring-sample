package com.example.demo.helper;

import com.example.demo.domain.entity.User;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public class UserAggregatorTestHelper implements ArgumentsAggregator {

    @Override
    public User aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
        User user = new User();
        user.setId(accessor.getLong(0));
        user.setName(accessor.getString(1));
        user.setAge(accessor.getString(2));
        return user;
    }
}
