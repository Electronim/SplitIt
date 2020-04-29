package com.example.splitit.model.wrappers;

import java.util.Objects;

public class GroupFriendCrossRefWrapper {
    private long groupId;
    private long friendId;

    public GroupFriendCrossRefWrapper(long groupId, long friendId) {
        this.groupId = groupId;
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupFriendCrossRefWrapper that = (GroupFriendCrossRefWrapper) o;
        return getGroupId() == that.getGroupId() &&
                getFriendId() == that.getFriendId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getFriendId());
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }
}
