package edu.utn.view;


import edu.utn.manager.GameManager;

public abstract class Stage implements View {


    @Override
    public void header() {
        System.out.println("\n     =================================================================================================================================================\n");
        System.out.println("\t\t\t\t     KOKUMO NO MONOGATARI (^o^)y\n");
        System.out.println("     =================================================================================================================================================\n");
    }

    public abstract void menu(GameManager manager);

    protected void printMessages(GameManager manager){
        manager.printMessages();
        manager.clearMessages();
    }

    protected void cleanConsole(){
        try{
            String operatingSystem = System.getProperty("os.name");
            if(operatingSystem.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        }catch(Exception e){
            System.out.println("\t\t\t"+e.getMessage());
        }
    }

    public void printError(GameManager manager){

        for(String message : manager.getOpError().getErrors().values()) {
            System.out.println("\t\t\t"+message);
        }
        manager.getOpError().getErrors().clear();
    }
    protected void exit(){
        footer();
        System.exit(0);
    }
    @Override
    public void footer() {

        System.out.println("\n     /**********************************************************************\n");
        System.out.println("\t\t\t*Student: Manuel Onetto Mouso\n");
        System.out.println("\t\t\t*Email: manumouso91@gmail.com\n");
        System.out.println("\t\t\t*Subjects: PROG/LAB III\n");
        System.out.println("\t\t\t*Career: TSSI-UTN-FRBA\n");
        System.out.println("\t\t\t*Date: 14/6/2021\n");
        System.out.println("     ***********************************************************************/\n");
    }


}
