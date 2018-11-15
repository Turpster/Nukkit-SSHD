package com.ryanmichela.sshd.implementations;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;
import com.ryanmichela.sshd.ConsoleShellFactory;
import xyz.wackster.nukkitutils.NukkitUtil;
import com.ryanmichela.sshd.SshdPlugin;
import xyz.wackster.nukkitutils.Conversation;
import xyz.wackster.nukkitutils.ConversationAbandonedEvent;
import xyz.wackster.nukkitutils.ManuallyAbandonedConversationCanceller;


import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;

public class SSHDCommandSender extends ConsoleCommandSender implements CommandSender {

    private final PermissibleBase perm = new PermissibleBase(this);
    private final SSHDConversationTracker conversationTracker = new SSHDConversationTracker();

    public void sendMessage(String message) {
        this.sendRawMessage(message);
    }

    public void sendRawMessage(String message) {
        if(ConsoleShellFactory.ConsoleShell.consoleReader == null) return;
        try {
            ConsoleShellFactory.ConsoleShell.consoleReader.println(NukkitUtil.stripColor(message));
        } catch (IOException e) {
            SshdPlugin.instance.getLogger().log(Level.SEVERE, "Error sending message to SSHDCommandSender", e);
        }
    }

    public void sendMessage(String[] messages) {
        Arrays.asList(messages).forEach(this::sendMessage);
    }

    public String getName() {
        return "SSHD CONSOLE";
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server console");
    }

    public boolean beginConversation(Conversation conversation) {
        return this.conversationTracker.beginConversation(conversation);
    }

    public void abandonConversation(Conversation conversation) {
        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        this.conversationTracker.abandonConversation(conversation, details);
    }

    public void acceptConversationInput(String input) {
        this.conversationTracker.acceptConversationInput(input);
    }

    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }

    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return this.perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return this.perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    public boolean isPlayer() {
        return false;
    }

    public Server getServer() {
        return Server.getInstance();
    }

}
