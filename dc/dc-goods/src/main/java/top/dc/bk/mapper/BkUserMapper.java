package top.dc.bk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import top.dc.pojo.bk.pojo.BkUser;

import java.util.List;

@Repository
public interface BkUserMapper extends BaseMapper<BkUser> {
    Integer findUserTotal(String userName);

    List<BkUser> findUserByPage(String userName, int startIndex, Integer pageSize);
}
