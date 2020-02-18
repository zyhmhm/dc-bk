package top.dc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import top.dc.pojo.User;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
