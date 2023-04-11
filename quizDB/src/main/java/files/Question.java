package files;

public class Question {

    private int qnsId;
    private int ExamId;
    private String Question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String CorrectAnswer;

    private String StudentAnswer;

    public Question(int qnsId, int examId, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this.qnsId = qnsId;
        ExamId = examId;
        Question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        CorrectAnswer = correctAnswer;
    }

    public String getStudentAnswer() {
        return StudentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        StudentAnswer = studentAnswer;
    }

    public Question() {
    }

    public int getQnsId() {
        return qnsId;
    }

    public void setQnsId(int qnsId) {
        this.qnsId = qnsId;
    }

    public int getExamId() {
        return ExamId;
    }

    public void setExamId(int examId) {
        ExamId = examId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }
}



