export interface R<T> {
  /**
   * 状态码
   */
  code: number;
  /**
   * 消息
   */
  msg: string;
  /**
   * 数据
   */
  data: T;
}


export interface RTCMsg {
    /**
   * 消息id
   */
  toUserId: any;
  /**
   * 消息类型
   */
  type: string;
  /**
   * 消息内容
   */
  content: any;
}
