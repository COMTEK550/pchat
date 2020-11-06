public interface Frontend {
    public void newTxtMsg(String msg, int conv);

    public void newErrMsg(String msg);

    public void newConMsg(int id, String[] users);

    public void newRegMsg(String name);
}
