import random


def aptitudeFunction (data, distribution):
    sum1 = 0
    sum2 = 0

    for i in range(20):
        if distribution[i] == 0:
            sum1 += data[i]
        elif distribution[i] == 1:
            sum2 += data[i]
    return abs(sum1 - sum2)
           
def mutation (line):
    for i in range(20):
        if (random.randint(0, 15) == 0):
            if (line[i] == 0):
                line[i] = 1
            else:
                line[i] = 0
    return line

def popMutation(population, mutationRate = 0.3):

    mutationAmount = round(random.randint(0, 10) * mutationRate)
    for i in range (mutationAmount):
        line = random.randint(0, 9) + 1
        column = random.randint(0, 19)
 
        if (population[line][column] == 0):
            population[line][column] = 1
        else:
            population[line][column] = 0
       
       
    return population

def getParents(population):
    father = []
    mother = []
    for i in range (2):
        newIndex1 = random.randint(0, 10)
        newIndex2 = random.randint(0, 10)
        option1 = population[newIndex1]
        option2 = population[newIndex2]
        lowestSum = 1000000
        
            
            
        bestOption = min(option1[20], option2[20])
        bestIndex = 0
        if (option1[20] == bestOption):
            bestIndex = newIndex1
        else:
            bestIndex = newIndex2

        if i == 0:
            father = population[bestIndex]
        else:
            mother = population[bestIndex]
    return father, mother

def getChild (father, mother):
    child = []
    for i in range (20):
        if (i < 10):
            child.append(father[i])
        else:
            child.append(mother[i])
    return child


def geneticAlgorithm(data, population):
    flagDone = False
    endIndex = 0
    iterations = 0
    while (iterations < 1000 and flagDone == False):
        newPopulation = [[0 for col in range(21)] for row in range(11)] 
        iterations += 1
        lowestSum = 1000000
        for i in range (11):
            if (population[i][20] < lowestSum):
                lowestSum = population[i][20]
                bestIndex = i
        newPopulation[0] = population[bestIndex]
        auxIndex = 1
        auxLineIndex = 1
        father = []
        mother = []

        while auxIndex < 10:
            father, mother = getParents(population)
            child1 = getChild(father, mother)
            child2 = getChild(mother, father)
            
            #newPopulation[auxIndex] = mutation(child1)
            newPopulation[auxIndex] = child1
            #newPopulation[auxIndex].append(aptitudeFunction(data, newPopulation[auxIndex]))
            auxIndex += 1
           # newPopulation[auxIndex] = mutation(child2)
            newPopulation[auxIndex] = child2
            #newPopulation[auxIndex].append(aptitudeFunction(data, newPopulation[auxIndex]))
            auxIndex += 1
            
        


            
        newPopulation = popMutation(newPopulation) 
        for i in range (1,11):
            newPopulation[i].append(aptitudeFunction(data, newPopulation[i]))

        

        for i in range(11):
            if (newPopulation[i][20] == 0):
                flagDone = True
                endIndex = i
                break
        population = newPopulation
        print ("Population")
        for i in range (11):
            print (population[i])
        print (iterations)
    return population[endIndex] 
        
    #while(qualityFunction(distribution) != 0):

         

def printWorkload(distribution):
    tasks1 = []
    tasks2 = []
    for i in range(20):
        if (distribution[i] == 0):
            tasks1.append(i)
        else:
            tasks2.append(i)
    print ("Tasks for worker 1: ")
    print (tasks1)
    print ("Tasks for worker 2: ")
    print (tasks2)

def printTasks(tasks):
    for i in range (len(tasks)):
        print ("Task: ")
        print (i)
        print ("Duration: ")
        print (tasks[i])

def main():
    entry = [5, 10, 15, 3, 10, 5, 2, 16, 9, 7, 5, 10, 15, 3, 10, 5, 2, 16, 9, 7]
    

    population = [[0 for col in range(21)] for row in range(11)]

    for i in range(11):
        for j in range(21):
            population[i][j] = random.randint(0, 1)
    for i in range (11):
        population[i][20] = aptitudeFunction(entry, population[i])
        print (population[i])
    
  
    endLine = geneticAlgorithm(entry, population)
    sum1 = 0
    sum2 = 0
    for i in range (len(endLine)-1):
       if (endLine[i] == 0):
           sum1 += entry[i]
       elif (endLine[i] == 1):
            sum2 += entry[i]
    print("")
    print ("Final distribution: ")
    print ("Sum1: ")
    print (sum1)
    print ("Sum2: ")
    print (sum2)
    printWorkload(endLine)
    #printTasks(entry)
    

    
        
    
    


if __name__ == "__main__":
    main()