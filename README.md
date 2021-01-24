# EXPRESS2EMF

## Structure

* __ExpressParser__: XText based parser for EXPRESS schema
* __ExpressEMF__: Emoflon TGG to convert EXPRESS model to Ecore model
* __de.htwdd.expressEMF.step__: XText based parser for SPF files (instances of EXPRESS model)
* __de.htwdd.expressEMF.step.ide__: Eclipse SPF editor
* __de.htwdd.expressEMF.step.ui__: Eclipse UI components SPF editor
* __IFC2CityGML__: Emoflon TGG to convert IFC4 to CityGML 3



## How to

### Set up

1. Install Eclipse modelling, PlantUML, Emoflon ...
    - [Eclipse modelling tools](https://www.eclipse.org/modeling/tools.php) from <https://www.eclipse.org/downloads/packages/>
    - [PlantUML](https://github.com/hallvard/plantuml) from update site <http://hallvard.github.io/plantuml>
    - [Emoflon (Ibex/Democles)](https://emoflon.org) from update site <https://emoflon.org/emoflon-ibex-updatesite/snapshot/updatesite/>
    - TODO download, installation
    - TODO Emoflon perspective
2. Checkout repository
3. Import projects in Eclipse: _File / Import Projects from File System or Archive_
    - select all child projects, skip root
    - Check option _Detect and configure project natures_
4. Build ExpressParser project
5. Build ExpressEMF project
6. Build StepParser project


### Run example

This run-trough may seem tedious, but shall walk you through the different projects in this repository and demonstrate how they play together. An application integrating the workflow will render these manual steps unnecessary.

1. ExpressParser:
    - Run `de.htwwdd.expressEMF.api.RunParser` (optional: schema location as argument, default schemas/IFC4.exp).
    - This will create an \*.xmi file in the same location as the original \*.exp file, for instance schemas/IFC4.xmi for the default.
2. ExpressEMF: 
    - Copy the \*.xmi file generated in step 1 to instances/src.xmi to be used as input for the transformation.
    - Run `org.emoflon.ibex.tgg.run.expressemf.INITIAL_FWD_App` to carry out the transformation.
    - This will create a file instances/trg.xmi as result of the transformation.
3. StepParser:
    - Copy the trg.xmi file from the previous step to testdata/IFC4.ecore (or use the one that's already there after assuring yourself that it has the same content as the trg.xmi from step 2)
    - Run `de.htwdd.expressEMF.step.api.RunParser` with the project-relative or absolute location of an IFC SPF file as argument
    - This will create two files \*.xmi and \*.inst.xmi named similar to the input \*.ifc file. The first contains the XText parsed SPF file conforming to Step.ecore and referencing into the meta model, e.g. IFC4.ecore. The second file contains the same model, but transformed to instantiate the meta model, e.g. IFC4.ecore. That is, the transformation has bound the meta model references.
4. IFC2CityGML:
    - Copy the \*.xmi file generated in step 2 to model/IFC4.ecore
    - Copy the \*.inst.xmi file generated in step 3 to ...
    - TODO complete

work in progress to verify and document installation, build and execution 




