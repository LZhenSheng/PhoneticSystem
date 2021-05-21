package com.example.phoneticsystem.util;

import android.content.Context;

import com.example.phoneticsystem.liteorm.User;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.List;

/**
 * LiteOrm数据库工具类
 */
public class LiteORMUtil {
    /**
     * 数据库工具类实例
     */
    private static LiteORMUtil instance;

    /**
     * 上下文
     */
    private final Context context;

    /**
     * 数据库实例
     */
    private final LiteOrm orm;

    /**
     * 构造方法
     *
     * @param context
     */
    private LiteORMUtil(Context context) {
        this.context = context;

        //获取偏好设置工具类
        PreferenceUtil sp = PreferenceUtil.getInstance(context);

        //创建数据库实例
        String databaseName = String.format("%s.db", sp.getUserId());
        orm = LiteOrm.newSingleInstance(context, databaseName);

        //设置调试模式
        orm.setDebugged(LogUtil.isDebug);
    }

    /**
     * 获取数据库工具类单例
     *
     * @param context
     * @return
     */
    public static LiteORMUtil getInstance(Context context) {
        if (instance == null) {
            instance = new LiteORMUtil(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * 创建或更新搜索历史
     *
     * @param data
     */
    public void createOrUpdate(User data) {
        orm.save(data);
    }

    /**
     * 查询所有搜索历史
     *
     * @return
     */
    public List<User> queryUser() {
        QueryBuilder<User> queryBuilder = new QueryBuilder<>(User.class)
                .appendOrderDescBy("created_at");
        return orm.query(queryBuilder);
    }

    /**
     * 删除搜索历史
     *
     */
    public void deleteAllUser() {
        orm.deleteAll(User.class);
    }
}

