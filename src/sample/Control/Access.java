package sample.Control;

import sample.Model.EmailAccount;
import sample.Model.EmailMessage;
import sample.Model.TreeFolder;


public class Access {

    private EmailMessage email;

    public EmailMessage getEmail() {
        return email;
    }

    public void setEmail(EmailMessage email) {
        this.email = email;
    }

    private TreeFolder treeFolder;

    public TreeFolder getTreeFolder() {
        return treeFolder;
    }

    public void setTreeFolder(TreeFolder treeFolder) {
        this.treeFolder = treeFolder;
    }

    private EmailAccount emailAccount;

    public EmailAccount getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(EmailAccount emailAccount) {
        this.emailAccount = emailAccount;
    }
}
