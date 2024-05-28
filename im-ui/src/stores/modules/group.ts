import {defineStore} from "pinia";
import {ChatGroupData, ChatGroupForm} from "@/api/im/chatGroup/type";
import {SysUserVo} from "@/api/login/login.data";

import {deleteGroup, exitGroup, getGroupChatList, getUserList} from "@/api/im/chatGroup";
import {confirm} from "@/boot/dialog";
import useUserStore from "@/stores/modules/user";
import {useFriendStore} from "@/stores/modules/friend";


export const useGroupStore = defineStore('group', () => {
    // 群聊列表
    const groupList = ref<ChatGroupData[]>([])
    // 当前群聊用户列表
    const currGroupUserList = ref<SysUserVo[]>([])
    // 当前群聊信息
    const currGroupInfo = ref<ChatGroupData>({})

    // 群聊查询条件
    const groupChatQueryFrom = ref<ChatGroupForm>({});

    /**
     * 获取当前用户群聊列表
     */
    const fatchGroupChatList = async () => {
        let res = await getGroupChatList(groupChatQueryFrom.value);
        if (res.code === 200) {
            groupList.value = res.rows
        } else {
        }
    }

    /**
     * 获取当前群聊用户列表
     */
    const fatchGroupChatUserList = async (group: ChatGroupData) => {

        currGroupInfo.value = group
        groupChatQueryFrom.value.id = group.id
        let res = await getUserList(groupChatQueryFrom.value);
        if (res.code === 200) {
            currGroupUserList.value = res.rows
        }
    }

    /**
     * 解散群聊
     */
    const dissolutionGroupChat = async (groupId: string | number, userId: string | number | null) => {
        confirm("解散群聊?", `解散此群,全体成员自动退出, 是否继续?`, () => {
            if (!userId) {
                // @ts-ignore
                userId = useUserStore().currUser?.userId
            }
            // @ts-ignore
            deleteGroup(groupId, userId).then((res: any) => {
                if (res.code === 200) {
                    fatchGroupChatList()
                } else {

                }
            })
        })
    }

    /**
     * 退出群聊
     */
    const quitGroupChat = async (groupId: string | number, userId: string | number | null) => {
        confirm("退出群聊", `是否退出此群?`, () => {
            if (!userId) {
                // @ts-ignore
                userId = useUserStore().currUser?.userId
            }
            // @ts-ignore
            exitGroup(groupId, userId).then((res: any) => {
                if (res.code === 200) {
                    fatchGroupChatList()
                } else {

                }
            })
        })
    }

    function setGroupList(v: Array<ChatGroupData>) {
        groupList.value = v
    }

    function getCurrGroupUserList(v: Array<SysUserVo>) {
        currGroupUserList.value = v
    }

    function setCurrGroupInfo(v: ChatGroupData) {
        currGroupInfo.value = v
    }

    return {
        groupList,
        currGroupUserList,
        currGroupInfo,
        setGroupList,
        getCurrGroupUserList,
        setCurrGroupInfo,

        fatchGroupChatList,
        fatchGroupChatUserList,
        dissolutionGroupChat,
        quitGroupChat,
        groupChatQueryFrom
    }
})
