### 2. TODO.txt
This file breaks down the development process into actionable tasks organized by class/component.

```text
PROJECT TODO LIST
=================

PHASE 1: MFN Class (Mathematical Logic)
---------------------------------------
[ ] Implement Field Definitions:
    - int m, int[] W, double[] C, int[] L, double[] R, double[] rho, double[] beta
    - ArrayList MPs
[ ] Implement Inner Class `Combinatorial`:
    - factorial(n)
    - binomialCoefficient(n, k)
[ ] Implement Constructor:
    - Validate vector lengths == m
    - Validate R and rho values (0 to 1)
    - Calculate `beta` vector using Formula (2) from [1]
[ ] Implement `getMPs(String fileName)`:
    - Read CSV file and populate `MPs` ArrayList
[ ] Implement `CDF(double[][] arPMF)`:
    - Create cumulative distribution function array
[ ] Implement `normalCDF(double z)`:
    - Approx standard normal distribution (n=100) using double factorial (!!)
[ ] Implement `normalICDF(double u)`:
    - Custom algorithm for Quantile function
    - Constraint: |normalCDF(x) - u| <= 10^-7
[ ] Implement Sample Size Calculator:
    - Fishman Formula (12b) [2]
[ ] Implement `randomSSV(int N, double[][] arCDF)`:
    - Generate N random vectors
    - Use Inverse CDF method or Chen and Asau Guide Table Method [3]

PHASE 2: SSVGeneratorGui Class (Interface)
------------------------------------------
[ ] Extend JFrame
[ ] Add FileChooser for selecting .csv files (MPs)
[ ] Add "Send Data" button
[ ] Implement Listener to trigger SSVGenerator logic

PHASE 3: SSVGenerator Class (Agent)
-----------------------------------
[ ] Extend Agent
[ ] Implement `setup()`:
    - Parse arguments (epsilon, delta)
    - Validate args (0 to 1)
    - Launch SSVGeneratorGui
[ ] Implement Action (triggered by GUI button):
    - Create MFN object
    - Display MFN parameters (W, C, L, R, rho) to console
    - Calculate N (sample size)
    - Call mfn.randomSSV() to generate data
[ ] Implement Agent Communication:
    - Search for "TT" agent in DF or by name
    - Create ACLMessage
    - Serialize and Send: MFN params, MPs file path, random SSVs
[ ] Implement Receive Behavior:
    - Wait for reply from TT
    - Display "Estimated network reliability"
    - doDelete() (terminate)

PHASE 4: TT Class (Transmission Time Agent)
-------------------------------------------
[ ] Extend Agent
[ ] Implement `setup()`:
    - Parse arguments (double d, double T)
[ ] Implement CyclicBehaviour/OneShotBehaviour:
    - Receive message from SSVGenerator
    - Deserialize data (MFN params, SSVs)
[ ] Implement File Output:
    - Write received SSVs to "SSV.csv"
[ ] Implement Reliability Estimation:
    - Loop through each SSV X
    - Calculate T(d, X) using MFN Formula (8) [1]
    - Compare T(d, X) <= T
    - Count successes / Total N
[ ] Implement Reply:
    - Send the calculated probability back to SSVGenerator

PHASE 5: Testing & Validation
-----------------------------
[ ] Test Network 1 (MPs0.csv):
    - Args: SSV(0.01, 0.01), TT(42, 15.5)
    - Expected Result: ~0.895
[ ] Test Network 2 (MPs1.csv):
    - Args: SSV(0.01, 0.01), TT(36, 120)
    - Expected Result: ~0.842
[ ] Verify SSV.csv creation