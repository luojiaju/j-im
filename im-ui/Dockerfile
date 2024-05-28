# 使用官方 nginx 镜像作为基础镜像
FROM nginx:latest

# 删除默认配置文件
RUN rm -rf /etc/nginx/conf.d/*

# 将本地 nginx 配置文件复制到容器中
COPY /dockerVolume_01/nginx/default.conf /etc/nginx/conf.d/default.conf

# 将打包后的静态文件复制到 nginx 静态文件目录
#COPY /dockerVolume_01/nginx/dist/spa /usr/share/nginx/html


# 暴露80端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]
