// 是否为空
import {Notify} from "quasar";

export function isNotEmpty(val: string): boolean | string {
  return !!val || '不能为空';
}

// 校验邮箱格式
export function isValidEmail(val: string): boolean | string {
  return /.+@.+\..+/.test(val) || '请输入合法的邮箱地址';
}

// 校验昵称6-16个字符,不能出现特殊字符
export function validNickname(val: string): boolean | string {
  return /^[\u4e00-\u9fa5A-Za-z0-9_]{6,16}$/.test(val) || '请输入6-16个字符的昵称,不能出现特殊字符';
}

//只校验中国手机号,考虑脱敏后的校验 182****4601
export function validPhone(val: string): boolean | string {
  // 去除脱敏后的手机号中的星号，获得实际的数字
  const realNumber = val.replace(/\*/g, 'x'); // 替换为任意数字
  // 进行校验
  return /^1[3456789]\d{9}$/.test(realNumber) || '请输入正确的手机号';
}


/**
 * 时间戳转字符串
 * @param timestamp
 */
export function timestampToISOString(timestamp: number): string {
    const date = new Date(timestamp);
    return date.toISOString();
}


/**
 * 在字符串指定位置插入内容
 */
export function insertContent(str: string, insertion: string, index: number): string {
    // 在指定索引位置插入内容
    if(!str){
        str+=''
    }
    return str.substring(0, index) + insertion + str.substring(index);
}


/**
 * 时间格式化
 * @param inputDateStr 时间字符串 列如："2024-05-16 22:28:15"
 * @return 两天内返回 昨天 22:28 今天：22:28，三天以上七天以内返回：周(1,2,3,4,5,6,7) 22:28 ，如果是更久以前则返回 05-16 22:28
 */
export function formatDate(inputDateStr: string): string {
    // 解析输入的日期字符串
    const inputDate = new Date(inputDateStr);
    if (isNaN(inputDate.getTime())) {
        throw new Error("Invalid date string");
    }

    // 获取当前日期和时间
    const now = new Date();

    // 去掉时间部分，只保留日期
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const inputDay = new Date(inputDate.getFullYear(), inputDate.getMonth(), inputDate.getDate());

    // 计算时间差（以毫秒为单位）
    const timeDiff = today.getTime() - inputDay.getTime();
    const dayDiff = timeDiff / (1000 * 60 * 60 * 24);

    // 获取输入日期的小时和分钟
    const hours = inputDate.getHours().toString().padStart(2, '0');
    const minutes = inputDate.getMinutes().toString().padStart(2, '0');
    const timePart = `${hours}:${minutes}`;

    if (dayDiff < 0) {
        // 输入日期在今天之后，不做处理
        throw new Error("Input date is in the future");
    } else if (dayDiff === 0) {
        // 今天
        return `今天 ${timePart}`;
    } else if (dayDiff === 1) {
        // 昨天
        return `昨天 ${timePart}`;
    } else if (dayDiff <= 7) {
        // 一周内
        const weekDays = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
        const dayOfWeek = weekDays[inputDay.getDay()];
        return `${dayOfWeek} ${timePart}`;
    } else {
        // 更久以前
        const month = (inputDate.getMonth() + 1).toString().padStart(2, '0');
        const day = inputDate.getDate().toString().padStart(2, '0');
        return `更久以前：${month}-${day} ${timePart}`;
    }
}

/**
 * 复制到剪切板
 */
export function copyToClipboard(text: string): void {
    navigator.clipboard.writeText(text).then(res=>{
        // 通知
        Notify.create({
            position: 'center',
            message: '复制成功',
            icon: 'content_copy',
            color: 'positive',
            timeout: 1000
        });
    }).catch(err=>{
        Notify.create({
            position: 'center',
            message: '复制失败',
            icon: 'content_copy',
            color: 'negative',
            timeout: 1000
        });
    })

}

/**
 * 判断是否是图片
 * @param filename 比如 https://www.conchship.com.cn/wp-content/uploads/2024/04/%E7%99%BE%E8%8A%B1.png
 */
export function isImage(filename:string){
    if(filename.endsWith('.png')||filename.endsWith('.jpg')||filename.endsWith('.jpeg')||filename.endsWith('.gif')||filename.endsWith('.bmp')||filename.endsWith('.webp')){
        return true
    }
    return false
}
