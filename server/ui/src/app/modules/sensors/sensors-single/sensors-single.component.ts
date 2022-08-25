import {
  AfterContentChecked,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {HumanService} from "@services/human.service";
import {SensorService} from "@services/sensor.service";
import {DataTableComponent} from "@components/data-table/data-table.component";
import {SensorMetadataService} from "@services/sensorMetadata.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SafeUrl} from "@angular/platform-browser";
import * as L from "leaflet";
import {DEFAULT_CENTER, LEAFLET_URL, NO_IMG,} from "src/app/constantes";
import {CLICK_MARKER} from "@models/icon.model";
import {AuthService} from "@services/auth.service";
import {DatePipe} from "@angular/common";
import {StateService} from "@services/state.service";

const widths = {
  LONG: 150,
  DOUBLE: 150,
  STRING: 300,
  DATE: 180,
};

@Component({
  selector: "app-sensors-single",
  templateUrl: "./sensors-single.component.html",
  styleUrls: ["./sensors-single.component.scss"],
})
export class SensorsSingleComponent implements OnInit, OnDestroy, AfterContentChecked {
  id?: number;
  metadata?: SensorMetadata<false>;
  editable: boolean = false;
  lnglat: Pos = DEFAULT_CENTER;

  // sensor: SensorExtended | undefined;
  parameters?: RecordParameters;
  records: (string | number)[][];
  edition: boolean = false;
  sensorsMetadata: SensorMetadata<true>[] = [];
  sensorForm: FormGroup;
  defaultUrl: SafeUrl = NO_IMG;
  photo?: File;
  create: boolean = false;
  init: boolean = false;
  marker: L.Marker;
  messageError: string = '';
  NO_IMG = NO_IMG;
  @ViewChild("sensorMetadataImg")
  sensorMetadataImg: ElementRef;
  @ViewChild(DataTableComponent, {static: true})
  public tb: DataTableComponent<string | number>;
  lastWidth = 1;
  private map: L.Map;
  private routeSub: Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private sensorService: SensorService,
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private route: ActivatedRoute,
    private router: Router,
    public auth: AuthService,
    private cdr: ChangeDetectorRef,
    private datepipe: DatePipe
  ) {
  }

  formatter: DTFormatter<(string | number)[]> = (d) => {
    return [
      <string>this.datepipe.transform(new Date(d[0]), 'yyyy-MM-dd HH:mm:ss'),
      ...d.slice(1)
    ];
  };

  resolveSensorMetadataExtended(smd: SensorMetadata<false>): SensorMetadata<true> | undefined {
    return this.sensorsMetadata.find(it => it.id == smd.id);
  }

  /**
   * All initializations relative to router changes
   */
  dynamicInit(): void {
    this.routeSub = this.route.params.subscribe((params) => {
      this.messageError = '';
      if (params["id"] == undefined || params["id"] == "create") {
        this.id = undefined;
        this.metadata = undefined;
        if (this.auth.isUser()) this.dynamicInitCreate();
        else this.router.navigate(["/login"]);
      } else {
        let id = +params["id"];
        if (isNaN(id)) { // invalid id
          this.router.navigate(["/error404"], {skipLocationChange: true});
          return;
        }
        this.id = id;
        this.dynamicInitUpdate();
      }
    });
  }

  getIdFormat() {
    if (this.id == undefined) return "#?????";
    let s = this.id + "";
    while (s.length < 5) s = "0" + s;
    return '#' + s;
  }

  dynamicInitCreate() {
    if (this.id != undefined) throw Error("Can't Create during update");
    this.enableMap();
    this.create = true;
    this.edition = true;
    this.sensorForm.enable();
    this.sensorForm.patchValue({
      sensorMetadata: undefined,
      name: "",
      indications: "",
      longitude: 0.0,
      latitude: 0.0,
      hiddenMessage: "Capteur indisponible",
      isHidden: 0,
      description: "",
      maintenanceDescription: "",
    });

    this.humanService
      .getLocation()
      .subscribe((pos) => this.markMap(pos), console.error);
  }

  dynamicInitUpdate() {
    if (this.id == undefined) throw Error("Can't Update during creation");
    this.disableMap();
    this.edition = false;
    this.create = false;
    this.sensorForm.disable();
    this.sensorService.getImage(this.id).subscribe(url=>{
       this.defaultUrl = url;
    }, console.error)

    this.sensorService.getById(this.id).subscribe(
      (sensor) => {
        setTimeout(() => {
          this.markMap({lat: sensor.latitude, lng: sensor.longitude});
        }, 500);
        this.metadata = sensor.metadata;
        this.editable = sensor.manageable;
        const metadata = this.resolveSensorMetadataExtended(sensor.metadata);
        console.log(metadata, this.metadata);
        this.sensorForm.patchValue({
          sensorMetadata: metadata,
          name: sensor.name,
          indications: sensor.indications,
          longitude: sensor.longitude.toFixed(5),
          latitude: sensor.latitude.toFixed(5),
          hiddenMessage: sensor.hiddenMessage,
          isHidden: sensor.isHidden,
          description: sensor.description,
          maintenanceDescription: sensor.maintenanceDescription,
        });
        this.cdr.detectChanges();
      },
      (err) => {
        if (err.status == 403) {
          this.router.navigate(["/sensors"]);
        } else {
          console.log("Error during sensor fetch");
          console.log(err);
        }
      }
    );
    this.tb.onChange();
  }

  initForm(): void {
    this.sensorsMetadata = [];
    this.sensorMetadataService.getAll().subscribe(
      (res) => {
        res.forEach((smd) => this.sensorsMetadata.push(smd));
        this.dynamicInit();
      },
      (err) => {
        console.error("Error during sensorMetdata fetch");
        console.error(err);
      }
    );
    this.sensorForm = this.formBuilder.group({
      sensorMetadata: ["", Validators.required],
      name: ["", Validators.required],
      indications: ["", Validators.required],
      hiddenMessage: ["", Validators.required],
      isHidden: [""],
      latitude: ["", Validators.required],
      longitude: ["", Validators.required],
      description: ["", Validators.required],
      maintenanceDescription: ["", Validators.required],
      photo: [""],
    });
  }

  /**
   * Initalize litle zone for sensor.
   */
  initMap(): void {
    this.map = L.map("map-sensors-single", {
      center: DEFAULT_CENTER,
      zoom: 1,
      zoomControl: false,
    });
    this.disableMap();
    const tiles = L.tileLayer(LEAFLET_URL);
    tiles.addTo(this.map);
    this.map.on("click", (event: any) => {
      if (!this.create) return;
      const {lat, lng} = event.latlng;
      this.markMap({
        lat: Math.round(lat * 10000) / 10000,
        lng: Math.round(lng * 10000) / 10000,
      });
    });
  }

  /**
   * Disable map edition.
   */
  disableMap() {
    this.map.dragging.disable();
    this.map.touchZoom.disable();
    this.map.doubleClickZoom.disable();
    this.map.scrollWheelZoom.disable();
    this.map.boxZoom.disable();
    this.map.keyboard.disable();
    if (this.map.tap) this.map.tap.disable();
    const mapRef = document.getElementById("map-sensors-single");
    if (mapRef?.style?.cursor) {
      (<any>mapRef).style.cursor = "default";
    }
    this.map.setMaxZoom(3);
    this.map.setMinZoom(3);
  }

  /**
   * Enable map edition.
   */
  enableMap(): void {
    this.map.dragging.enable();
    this.map.touchZoom.enable();
    this.map.doubleClickZoom.enable();
    this.map.scrollWheelZoom.enable();
    this.map.boxZoom.enable();
    this.map.keyboard.enable();
    if (this.map.tap) this.map.tap.enable();
    const mapRef = document.getElementById("map-sensors-single");
    if (mapRef?.style?.cursor) {
      (<any>mapRef).style.cursor = 'url("/assets/markers/marker-gray.png") 12 41, pointer !important';
    }
    this.map.setMaxZoom(18);
    this.map.setMinZoom(1);
  }

  /**
   * Remove previous marker and add one to lat and lng then move the map to the coords.
   * @param {lat, lng}: {lat: number, lng: number}
   */
  markMap({lat, lng}: { lat: number; lng: number }) {
    if (this.marker) this.map.removeLayer(this.marker);
    this.marker = L.marker({lat, lng}, {icon: CLICK_MARKER});
    this.marker.addTo(this.map);
    this.map.flyTo({lat, lng});
    this.sensorForm.controls["latitude"].setValue(lat.toFixed(5));
    this.sensorForm.controls["longitude"].setValue(lng.toFixed(5));
    this.map.invalidateSize();
  }

  /**
   * Initalize component.
   */
  ngOnInit(): void {
    this.initForm();
    this.initMap();
  }

  /**
   * Clear All.
   */
  ngOnDestroy(): void {
    console.log("OnDestroy Single")
    this.routeSub?.unsubscribe();
    // Can't use twice the same map
    // this.map.remove();
  }

  /**
   * Handler for pagination change on datatable
   *
   * @param info information about event
   */
  onChange(info: DataTableNatigateEvent): void {
    if (this.id == undefined)
      throw Error("Can't change datatable info during create");
    this.sensorService
      .getParametersOfId(this.id, info)
      .subscribe((parameters) => {
        this.parameters = parameters;
      });
  }

  /**
   * Change sensor metadata.
   */
  onChangeMetadata(event: SensorMetadata<false>) {
    this.metadata = event;
  }

  /**
   * Trigger on button after edit for save changes.
   */
  onSave() {
    this.sensorForm.disable();
    const form = Object.assign({}, this.sensorForm.value);
    const data = new FormData();
    if (this.photo) data.append("photo", this.photo, "photo.png");

    data.append(
      "sensor",
      new Blob(
        [
          JSON.stringify({
            sensorMetadataId: form["sensorMetadata"]?.id,
            name: form["name"],
            indications: form["indications"],
            longitude: +form["longitude"],
            latitude: +form["latitude"],
            hiddenMessage: "Aucun",
            isHidden: false,
            description: form["description"],
            maintenanceDescription: form["maintenanceDescription"],
          }),
        ],
        {
          type: "application/json; charset=utf-8",
        }
      )
    );

    if (this.create)
      this.sensorService.create(data).subscribe(
        sensor => {
          console.log(sensor);
          /* TODO patch map already exist */
          this.router.navigate(["/sensors", sensor.id]);
        },
        err => {
          this.sensorForm.enable();
          this.messageError = err?.error?.message || 'Unknow';
        });
    else
      this.sensorService
        .update(<number>this.id, data)
        .subscribe(res => {
          this.edition = false;
        }, err => {
          this.sensorForm.enable();
          this.messageError = err?.error?.message || 'Unknow';
        });
  }

  /**
   * Switch to edition mode.
   */
  onEdition(): void {
    this.edition = !this.edition;
    if (this.edition) {
      this.sensorForm.enable();
      this.sensorForm.controls.sensorMetadata.disable();
      this.sensorForm.controls.latitude.disable();
      this.sensorForm.controls.longitude.disable();
    } else {
      this.sensorForm.disable();
    }
  }

  /**
   * function used by datatable for format.
   * @param index index of row
   * @param value value of row
   * @returns Value usable by datatable
   */
  format(index: number, value: string | number): string | Icon {
    if (this.type(index) === "DOUBLE") return Number(value).toExponential(3);
    if (value === null || value === undefined) return "";
    return value.toString();
  }

  /**
   * Widths of Datatable row.
   * @returns
   */
  sizes(): number[] {
    let width = this.metadata?.parameters?.width || 0;
    let type = this.metadata?.parameters?.formats || [];
    let arr = [];
    for (let i = 0; i < width; i++) {
      arr.push(widths[type[i]]);
    }
    return arr;
  }

  /**
   * Return parameter type of given index.
   * @param index index in meta parameter.
   * @returns type as string.
   */
  type(index: number): "LONG" | "DOUBLE" | "STRING" | "DATE" | undefined {
    return this.metadata?.parameters?.formats[index];
  }

  /**
   * Create link to google map point from lat and lng.
   * @returns The link
   */
  coordLink(): string {
    let base = "https://www.google.com/maps/search/?api=1";
    const form = Object.assign({}, this.sensorForm.value);
    return `${base}&query=${form["latitude"]},${form["longitude"]}`;
  }

  getSize() {
    return this.init && this.sensorMetadataImg ? this.sensorMetadataImg.nativeElement.offsetWidth : 1;
  }

  ngAfterContentChecked(): void {
    setTimeout(() => {
      this.init = true
    });
  }

  onQRCode() {
    if (this.id) this.sensorService.qrcode(this.id);
  }
}
