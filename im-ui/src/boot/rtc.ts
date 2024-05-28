class WebRTC {
    private peerConnection: RTCPeerConnection;

    constructor() {
        this.peerConnection = new RTCPeerConnection({
            // iceServers配置（可根据实际情况填写）
            iceServers: [
                { urls: "stun:stun.l.google.com:19302" },
                { urls: "turn:numb.viagenie.ca", username: "your_username", credential: "your_password" }
            ]
        });
    }

    /**
     * 添加本地媒体流
     * @param stream MediaStream
     */
    public addLocalStream(stream: MediaStream): void {
        this.peerConnection.addTrack(stream.getTracks()[0], stream);
    }

    /**
     * 创建Offer SDP
     * @returns Promise<RTCSessionDescriptionInit>
     */
    public createOffer(): Promise<RTCSessionDescriptionInit> {
        return this.peerConnection.createOffer().then((desc) => {
            return this.peerConnection.setLocalDescription(desc).then(() => desc);
        });
    }

    /**
     * 设置远程Offer/Answer
     * @param description RTCSessionDescriptionInit
     */
    public setRemoteDescription(description: RTCSessionDescriptionInit): Promise<void> {
        return this.peerConnection.setRemoteDescription(new RTCSessionDescription(description));
    }

    /**
     * 获取本地描述
     * @returns Promise<RTCSessionDescription>
     */
    public getLocalDescription(): Promise<RTCSessionDescription> {
        return this.peerConnection.createOffer();
    }

    /**
     * 添加ICE候选
     * @param candidate RTCIceCandidate
     */
    public addIceCandidate(candidate: RTCIceCandidateInit): void {
        this.peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
    }

    /**
     * 监听ICE候选事件
     * @param callback (candidate: RTCIceCandidate) => void
     */
    public onIceCandidate(callback: (candidate: RTCIceCandidate) => void): void {
        this.peerConnection.onicecandidate = event => {
            if (event.candidate) {
                callback(event.candidate);
            }
        };
    }

    /**
     * 关闭PeerConnection
     */
    public close(): void {
        this.peerConnection.close();
    }
}

export default WebRTC;
