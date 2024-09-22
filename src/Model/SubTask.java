package Model;

public class SubTask extends Task {
    Epic epic;

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }

    @Override
    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
