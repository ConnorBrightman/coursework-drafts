/* **********************
 * CSC-20004 COURSEWORK *
 * 2022/23 First sit    *
 * **********************/
package uk.ac.keele.csc20004.sample;

import uk.ac.keele.csc20004.factory.AbstractComputerCompany;
import uk.ac.keele.csc20004.factory.ComputerAssembler;
import uk.ac.keele.csc20004.factory.ComputerBox;
import uk.ac.keele.csc20004.factory.ComputerCompany;
import uk.ac.keele.csc20004.factory.DeliveryArea;
import uk.ac.keele.csc20004.factory.LaptopAssembler;
import uk.ac.keele.csc20004.factory.ServerAssembler;
import uk.ac.keele.csc20004.factory.computers.Computer;

/**
 * An example of a simualted company, in a sequential setting.
 */
public class SampleSequentialCompany extends AbstractComputerCompany {
    DeliveryArea deliveries = new SampleDeliveryArea();

    /**
     * Constructor for the company.
     * 
     * @param boxSize the number of computers necessary to fill a box
     */
    public SampleSequentialCompany(int boxSize) {
        super(boxSize);
    }

    @Override
    public void enqueueForDomesticDelivery(ComputerBox b) throws InterruptedException {
        deliveries.pushDomestic(b);
    }

    @Override
    public void enqueueForInternationalDelivery(ComputerBox b) throws InterruptedException {
        deliveries.pushInternational(b);
    }

    @Override
    public ComputerBox sell() throws InterruptedException {
        return deliveries.poll();
    }

    /**
     * The method to implement a demo of the oprtations of the company in a
     * sequential setting.
     * Note that this implementation *does not* necessarily meet the requirements
     * for the coursework.
     * The purpose of this code is only to show how the classes in the provided 
     * starting code may fit together 
     * 
     * @param args to provide args from the command line (not used)
     */
    public static void main(String[] args) {
        int boxSize = 3;
        ComputerCompany company = new SampleSequentialCompany(boxSize);

        // as many domestic workers as needed to prepare a box
        ComputerAssembler domesticWorkers[] = new ComputerAssembler[] {
                new SampleSequentialAssembler(company),
                new LaptopAssembler(company),
                new ServerAssembler(company)
        };

        // as many domestic workers as needed to prepare a box
        ComputerAssembler internationalWorkers[] = new ComputerAssembler[] {
                new SampleSequentialAssembler(company),
                new LaptopAssembler(company),
                new ServerAssembler(company)
        };

        try {
            while (true) {
                int numBoxes = 10;

                for (int i = 0; i < numBoxes; i++) {
                    for (int w = 0; w < boxSize; w++) {
                        Computer dc = domesticWorkers[w].assemble();
                        company.enqueueForDomesticPackaging(dc);

                        Computer ic = internationalWorkers[w].assemble();
                        company.enqueueForInternationalPackaging(ic);
                    }
                }

                for (int i = 0; i < numBoxes; i++) {
                    ComputerBox db = company.getDomesticPackage();
                    company.enqueueForDomesticDelivery(db);

                    ComputerBox ib = company.getInternationalPackage();
                    company.enqueueForInternationalDelivery(ib);
                }

                for (int i = 0; i < 2 * numBoxes; i++) {
                    ComputerBox b = company.sell();

                    System.out.println("Sold: " + b);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
